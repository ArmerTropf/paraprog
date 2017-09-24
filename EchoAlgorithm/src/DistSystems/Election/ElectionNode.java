package DistSystems.Election;

import DistSystems.Interfaces.Node;
import DistSystems.Interfaces.NodeAbstract;
import DistSystems.Interfaces.SpanningTreeNode;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by Hendrik Mahrt on 23.09.17.
 */
public class ElectionNode extends NodeAbstract {

    private final int nodeRank;
    private boolean sleeping = true;
    private Node neighbourAwakenMe;
    private int numberOfMessagesReceived = 0;
    private SpanningTreeNode spanningTreeNode = new SpanningTreeNode(this);
    private int rank = 0;

    /**
     * Constructor of an election node.
     *
     * @param name
     * @param initiator
     * @param barrier
     */
    public ElectionNode(String name, int rank, boolean initiator, CyclicBarrier barrier) {
        super(name, initiator, barrier);
        this.rank = rank;
        this.nodeRank = rank;

        if (initiator) {
            System.out.println(name + " is an initiator");
            sleeping = false;
        }
    }

    @Override
    public synchronized void setupNeighbours(Node... neighbours) {
        Collections.addAll(this.neighbours, neighbours);
        notifyAll();
    }

    public void printNeighbours() {
        System.out.print(this.toString() + ": ");
        neighbours.forEach((node) -> System.out.print(node + " | "));
        System.out.println();
    }

    @Override
    public void run() {
        try {
            sendHelloToAllNeighbours();

            if (initiator)
                wakeupNeighbours();
            else if (hasWakeupHappened())
                wakeupNeighbours();
            else
                return; // stop thread when no wakeup happened

            waitForAllMessages();

            if (isThisNodeWinner())
                printTree();
            else
                sendEcho();

        } catch (BrokenBarrierException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isThisNodeWinner() {
        return nodeRank == rank;
    }

    private void sendHelloToAllNeighbours() throws BrokenBarrierException, InterruptedException {
        Set<Node> initialNeighbours = getCopyOfNeighbours();
        initialNeighbours.parallelStream().forEach((node -> node.hello(this)));
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
                .forEach(node -> node.wakeup(this, this.rank));
    }

    private synchronized void waitForAllMessages() throws InterruptedException {
        while(!receivedAMessageFromEveryNeighbour())
            wait();
    }

    private boolean receivedAMessageFromEveryNeighbour() {
        int expectedNumberOfMessages = neighbours.size();
        return numberOfMessagesReceived == expectedNumberOfMessages;
    }

    private void printTree() {
        System.out.println(spanningTreeNode.toString());
    }

    private void sendEcho() {
        System.out.println(this + " sends echo to " + neighbourAwakenMe + " thread: " + currentThread().getName());
        neighbourAwakenMe.echo(this, this.rank, spanningTreeNode);
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
    public synchronized void wakeup(Node neighbour, int neighourRank) {

        if (!sleeping) {

            if (neighourRank == this.rank) {
                System.out.println(this + " already woken up with rank: " + neighourRank + " thread: " + currentThread().getName());
                ++numberOfMessagesReceived;
            } else if (neighourRank  < this.rank) {
                // do nothing
            } else {
                // conquer
                System.out.println(this + " conquered by " + neighbour + " rank: " + neighourRank + " thread: " + currentThread().getName());
                neighbourAwakenMe = neighbour;
                this.rank = neighourRank;
                numberOfMessagesReceived = 1;
                wakeupNeighbours(); // problem?
            }

        } else {
            // first wakeup
            System.out.println(this + " woken up by " + neighbour + " rank: " + neighourRank + " thread: " + currentThread().getName());

            neighbourAwakenMe = neighbour;
            this.rank = neighourRank;
            sleeping = false;
        }

        notifyAll();
    }

    @Override
    public synchronized void echo(Node neighbour, int neighbourRank, Object data) {

        if (neighbourRank == this.rank) {
            ++numberOfMessagesReceived;
            spanningTreeNode.addPrecursor((SpanningTreeNode) data);
            System.out.println(this + " received an echo from " + neighbour + " rank: " + neighbourRank + " number of messages received: " + numberOfMessagesReceived + " thread: " + currentThread().getName());
        }

        notifyAll();
    }
}
