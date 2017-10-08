package DistSystems.Election;

import DistSystems.Echo.EchoNode;
import DistSystems.Interfaces.ElectionNode;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by Hendrik Mahrt on 23.09.17.
 */
public class ElectionNodeImp extends EchoNode implements ElectionNode {

    private final int nodeRank;
    private int currentRank = 0;
    private boolean explored = false;
    private int numberOfExploreMessagesReceived = 0;
    private ElectionNode neighbourExploredMe;
    private int numberOfEchosReceived = 0;
    private boolean leaderIsDetermined = false;
    private ElectionNode leader = null;
    private boolean conquered = false;

    public ElectionNodeImp(String name, int rank, boolean initiator, CyclicBarrier barrier) {
        super(name, initiator, barrier);
        currentRank = rank;
        nodeRank = rank;
    }

    @Override
    public void run() {
        try {
            sendHelloToAllNeighbours();
            waitForOtherNodes();
        
            // if initiator: receive explore message from 'virtual' neighbour
            if (initiator)
                leaderExplore(null, this.currentRank);
            
            // wait for explore message incoming
            if (hasExploreHappened())
                // send explore message to all other neighbours
                exploreNeighbours();
            else
                return; // stop thread when no explore happened

            // wait for explore/echos from all neighbours
            waitForAllExploresAndResendExplorersIfCaputured();

            if (!isThisNodeWinner())
            sendLeaderEcho(); // send leaderecho

            // if i am conquered: send a new echo to new neighbour. send explores to all other neighbours

            waitForLeaderVictoryMessage();

            sendVictoryMessage();

            // wait here for normal echo algorithm or being conquered -> send new echo and explorers
            if (isThisNodeWinner())
                System.out.println(this + " is winner. start normal echo"); // start normal echo here and obtain spanningtree

            // start echo with winner as only leader

            // reset states. goto 0.

        } catch (BrokenBarrierException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void waitForLeaderVictoryMessage() throws InterruptedException {
        while(!leaderIsDetermined) {
            // if conquered -> send leaderEcho and explore messages

            if (conquered)
                resendExplorers();

            if (isThisNodeWinner()) {
                leader = this;
                leaderIsDetermined = true;
                return;
            }

            wait();
        }
    }

    private void sendVictoryMessage() {
        neighbours.parallelStream()
                .filter(node -> !node.equals(neighbourExploredMe))
                .forEach(node -> ((ElectionNode)node).leaderVictory(this, leader));
    }

    private void sendLeaderEcho() {
        System.out.println(this + " sends leaderecho to " + neighbourExploredMe + " thread: " + currentThread().getName());
        neighbourExploredMe.leaderEcho(this, this.currentRank);
    }

    private synchronized void waitForAllExploresAndResendExplorersIfCaputured() throws InterruptedException {
        while(!receivedAMessageFromEveryNeighbour()) {
            if (conquered)
                resendExplorers();

            wait();
        }

    }

    private synchronized void resendExplorers() {
        System.out.println(this + " was conquered and needs to resend explorers");
        exploreNeighbours();
        conquered = false;
    }

    private boolean receivedAMessageFromEveryNeighbour() {
        int expectedNumberOfMessages = initiator ? neighbours.size() + 1 : neighbours.size();
        return numberOfExploreMessagesReceived + numberOfEchosReceived == expectedNumberOfMessages;
    }

    private synchronized boolean hasExploreHappened() throws InterruptedException {
        while(!explored) {
            wait(1000);

            if (!explored) {
                System.out.println("explore timeout. " +
                        "Assume missing connection to initiator. shutting down node: " + this);
                return false;
            }
        }
        return true;
    }

    private void exploreNeighbours() {
        neighbours.parallelStream()
                .filter(node -> !node.equals(neighbourExploredMe))
                .forEach(node -> ((ElectionNode)node).leaderExplore(this, this.currentRank));
    }

    private boolean isThisNodeWinner() {
        return nodeRank == currentRank;
    }

    /* ElectionNode interface implementation */

    @Override
    public synchronized void leaderExplore(ElectionNode neighbour, int neighboursRank) {

        if (!explored) {
            System.out.println(this + " explored with currentRank: " + neighboursRank + ". neighbour: " + neighbour  + " number of messages received: " + numberOfExploreMessagesReceived + " thread: " + currentThread().getName());
            explored = true;
            ++numberOfExploreMessagesReceived;
            neighbourExploredMe = neighbour;
            currentRank = neighboursRank;
        } else {
            if (currentRank == neighboursRank) {
                ++numberOfExploreMessagesReceived;
                System.out.println(this + " already explored with currentRank: " + neighboursRank + ". neighbour: " + neighbour  + " number of messages received: " + numberOfExploreMessagesReceived + " thread: " + currentThread().getName());
            } else if (currentRank < neighboursRank) {
                // conquered
                System.out.println(this + " conquered by " + neighbour + " currentRank: " + neighboursRank + " number of messages received: 1 thread: " + currentThread().getName());
                neighbourExploredMe = neighbour;
                currentRank = neighboursRank;
                numberOfExploreMessagesReceived = 1;
                numberOfEchosReceived = 0;
                conquered = true;
            } else {
                System.out.println(this + " explored by " + neighbour + " with lower currentRank " + neighboursRank  + " number of messages received: " + numberOfExploreMessagesReceived +". ignore!");
            }
        }

        notifyAll();
    }

    @Override
    public synchronized void leaderEcho(ElectionNode neighbour, int neighboursRank) {
        ++numberOfEchosReceived;
        System.out.println(this + " received leader echo by " + neighbour + " with rank " + neighboursRank  + ". number of echos received: " + numberOfEchosReceived);
        notifyAll();
    }

    @Override
    public synchronized void leaderVictory(ElectionNode neighbour, ElectionNode victor) {
        System.out.println(this + " received message that " + victor + " is leader");
        leader = victor;
        leaderIsDetermined = true;
        notifyAll();
    }
}
