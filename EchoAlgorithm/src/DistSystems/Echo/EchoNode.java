package DistSystems.Echo;

import DistSystems.Interfaces.Node;
import DistSystems.Interfaces.NodeAbstract;
import DistSystems.Interfaces.SpanningTreeNode;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Echo node implementation.
 * Created by Michael Guenster, Andre Schriever, Hendrik Mahrt on 17.09.2017.
 */
public class EchoNode extends NodeAbstract {

    private boolean sleeping = true;
    private Node neighbourAwakenMe;
    private int numberOfMessagesReceived = 0;

    private SpanningTreeNode spanningTreeNode = new SpanningTreeNode(this);

    public EchoNode(String name, boolean initiator, CyclicBarrier barrier) {
        super(name, initiator, barrier);

        if (initiator)
            System.out.println(name + " is an initiator");
    }

    @Override
    public synchronized void setupNeighbours(Node... neighbours) {
        Collections.addAll(this.neighbours, neighbours);
        notifyAll();
    }

    public synchronized void printNeighbours() {
        System.out.print(this.toString() + ": ");
        neighbours.forEach((node) -> System.out.print(node + " | "));
        System.out.println();
    }

    @Override
    public void run() {
        try {
            sendHelloToAllNeighbours();

            if (initiator)
                wakeup(null); // let initiator wakeup itself

            if (hasWakeupHappened())
                wakeupNeighbours();
            else
                return; // stop thread because no initiator in tree

            waitForAllMessages();

            if (initiator)
                printTree();
            else
                sendEcho();

        } catch (BrokenBarrierException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void sendHelloToAllNeighbours() throws BrokenBarrierException, InterruptedException {
        // Get copy of neighbours to prevent modification of collection while iterating over
        Set<Node> initialNeighbours = getCopyOfNeighbours();

        // send hello to all neighbours
        initialNeighbours.parallelStream().forEach((node -> node.hello(this)));

        // wait for all neighbours to do the same
        waitForOtherNodes();
    }

    private Set<Node> getCopyOfNeighbours() {
        Set<Node> neighboursCopy = new HashSet<>();
        synchronized (this) {
            neighboursCopy.addAll(neighbours);
        }

        return neighboursCopy;
    }

    private void waitForOtherNodes() throws BrokenBarrierException, InterruptedException {
        barrier.await();
    }

    private synchronized boolean hasWakeupHappened() throws InterruptedException {
        while(sleeping) {
            wait(1000);

            // if i am still sleeping, assume, i can not be reached. shut down.
            if (sleeping) {
                System.out.println("wakeup timeout. " +
                        "Assume missing connection to initiator. shutting down node: " + this);
                return false;
            }
        }
        return true;
    }

    private void wakeupNeighbours() {
        // wake up all neighbours except the one who woke me up
        neighbours.parallelStream()
                .filter(node -> !node.equals(neighbourAwakenMe))
                .forEach(node -> node.wakeup(this));
    }

    private synchronized void waitForAllMessages() throws InterruptedException {
        while(!receivedAMessageFromEveryNeighbour())
            wait();
    }

    private synchronized boolean receivedAMessageFromEveryNeighbour() {
        // initiator needs an extra message, because he was woken up by a virtual neighbour
        // which increased its counter anyway.
        int expectedNumberOfMessages = initiator ? neighbours.size() + 1 : neighbours.size();
        return numberOfMessagesReceived == expectedNumberOfMessages;
    }

    private void printTree() {
        System.out.println(spanningTreeNode.toString());
    }

    private void sendEcho() {
        System.out.println(this + " sends echo to " + neighbourAwakenMe + " thread: " + currentThread().getName());
        neighbourAwakenMe.echo(this, spanningTreeNode);
    }

    /* Node interface implementation */

    @Override
    public synchronized void hello(Node neighbour) {
        System.out.println(this + " got hello from " + neighbour + " thread: " + currentThread().getName());

        if (!neighbours.contains(neighbour))
            neighbours.add(neighbour);

        notifyAll();
    }

    @Override
    public synchronized void wakeup(Node neighbour) {
        ++numberOfMessagesReceived;
        System.out.println(this + " woken up by " + neighbour + " thread: " + currentThread().getName());

        if (!sleeping) {
            System.out.println(this + " already woken up" + " thread: " + currentThread().getName());
            notifyAll();
            return;
        }

        // remember neighbour who woke me up for echo
        neighbourAwakenMe = neighbour;

        // change status to sleeping
        sleeping = false;

        notifyAll();
    }

    @Override
    public synchronized void echo(Node neighbour, Object data) {
        ++numberOfMessagesReceived;

        // add received spanningTreeNode as precursor into my own spanningTreeNode
        spanningTreeNode.addPrecursor((SpanningTreeNode) data);

        System.out.println(this + " received an echo from " + neighbour + " thread: " + currentThread().getName());

        notifyAll();
    }
}
