package DistSystems.Echo;

import DistSystems.Election.ElectionNode;
import DistSystems.Interfaces.Node;
import DistSystems.Interfaces.NodeAbstract;
import DistSystems.Interfaces.SpanningTreeNode;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by Hendrik Mahrt on 17.09.2017.
 */
public class EchoNode extends NodeAbstract {

    private boolean sleeping = true;
    private Node neighbourAwakenMe;
    private int numberOfMessagesReceived = 0;
    private SpanningTreeNode spanningTreeNode = new SpanningTreeNode(this);

    public EchoNode(String name, boolean initiator, CyclicBarrier barrier) {
        super(name, initiator, barrier);
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
                wakeup(null, 0); // let initiator wakeup itself

            if (hasWakeupHappened())
                wakeupNeighbours();
            else
                return; // stop thread

            waitForAllMessages();

            if (initiator)
                printTree();
            else
                sendEcho();

        } catch (BrokenBarrierException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendHelloToAllNeighbours() throws BrokenBarrierException, InterruptedException {
        new CopyOnWriteArraySet<>(neighbours)
                .parallelStream().
                forEach((node -> node.hello(this)));
        waitForOtherNodes();
    }

    private void waitForOtherNodes() throws BrokenBarrierException, InterruptedException {
        barrier.await();
    }

    private synchronized boolean hasWakeupHappened() throws InterruptedException {
        while(sleeping) {
            wait(1000);

            if (sleeping) {
                System.out.println("wakeup timeout. " +
                        "Assume missing connection to initiator. shutting down node: " + this);
                return false;
            }
        }
        return true;
    }

    private void wakeupNeighbours() {
        neighbours.parallelStream()
                .filter(node -> !node.equals(neighbourAwakenMe))
                .forEach(node -> node.wakeup(this, 0));
    }

    private synchronized void waitForAllMessages() throws InterruptedException {
        while(!receivedAMessageFromEveryNeighbour())
            wait();
    }

    private boolean receivedAMessageFromEveryNeighbour() {
        int expectedNumberOfMessages = initiator ? neighbours.size() + 1 : neighbours.size();
        return numberOfMessagesReceived == expectedNumberOfMessages;
    }

    private void printTree() {
        System.out.println(spanningTreeNode.toString());
    }

    private void sendEcho() {
        System.out.println(this + " sends echo to " + neighbourAwakenMe + " thread: " + currentThread().getName());
        neighbourAwakenMe.echo(this, 0, spanningTreeNode);
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
    public synchronized void wakeup(Node neighbour, int rank) {
        ++numberOfMessagesReceived;
        System.out.println(this + " woken up by " + neighbour + " thread: " + currentThread().getName());

        if (!sleeping) {
            System.out.println(this + " already woken up" + " thread: " + currentThread().getName());
            notifyAll();
            return;
        }

        neighbourAwakenMe = neighbour;
        sleeping = false;

        notifyAll();
    }

    @Override
    public synchronized void echo(Node neighbour, int rank, Object data) {
        ++numberOfMessagesReceived;
        spanningTreeNode.addPrecursor((SpanningTreeNode) data);
        System.out.println(this + " received an echo from " + neighbour + " thread: " + currentThread().getName());

        notifyAll();
    }

    @Override
    public void leaderElected(ElectionNode electionNode) {

    }
}
