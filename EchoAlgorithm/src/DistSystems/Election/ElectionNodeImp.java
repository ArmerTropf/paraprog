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
    private int rank = 0;
//    private boolean sleeping = true;
//    private Node neighbourAwakenMe;
//    private int numberOfMessagesReceived = 0;
//    private int numberOfEchosReceived = 0;

    public ElectionNodeImp(String name, int rank, boolean initiator, CyclicBarrier barrier) {
        super(name, initiator, barrier);
        this.rank = rank;
        this.nodeRank = rank;
    }

    @Override
    public void run() {
        try {
            sendHelloToAllNeighbours();
            waitForOtherNodes();
        
            // if initiator: receive explore message from 'virtual' neighbour
            if (initiator)
                leaderExplore(null, this.rank);
            
            // wait for explore message incoming
            if (hasWakeupHappened())
                // send explore message to all other neighbours
                wakeupNeighbours();
            else
                return; // stop thread when no explore happened

            // wait for explore/echos from all neighbours
            waitForAllMessages();
                        
            if (isThisNodeWinner())
                printTree(); // start normal echo here and obtain spanningtree
            else
                sendEcho(); // send leaderecho
                
            // wait here for normal echo algorithm or being conquered -> send new echo and explorers

        } catch (BrokenBarrierException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isThisNodeWinner() {
        return nodeRank == rank;
    }

    @Override
    public void leaderExplore(ElectionNode neighbour, int neighboursRank) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void leaderEcho(ElectionNode neighbour, int neighboursRank) {
        // TODO Auto-generated method stub
        
    }

    /* Node interface implementation */

//    @Override
//    public synchronized void hello(Node neighbour) {
//        System.out.println(this + " got hello from " + neighbour + " thread: " + currentThread().getName());
//
//        if (!neighbours.contains(neighbour))
//            neighbours.add(neighbour);
//
//        notifyAll();
//    }
//
//    @Override
//    public synchronized void wakeup(Node neighbour, int neighourRank) {
//
//        if (!sleeping) {
//
//            if (neighourRank == this.rank) {
//                System.out.println(this + " already woken up with rank: " + neighourRank + ". neighbour: " + neighbour  + " number of messages received: " + ++numberOfMessagesReceived + " thread: " + currentThread().getName());
////                ++numberOfMessagesReceived;
//            } else if (neighourRank  < this.rank) {
//                // do nothing
//                System.out.println(this + " woken up by " + neighbour + " with lower rank " + neighourRank  + " number of messages received: " + numberOfMessagesReceived +". ignore!");
//            } else {
//                // conquer
//                System.out.println(this + " conquered by " + neighbour + " rank: " + neighourRank  + " number of messages received: 1 thread: " + currentThread().getName());
//                neighbourAwakenMe = neighbour;
//                this.rank = neighourRank;
//                numberOfMessagesReceived = 1;
//                numberOfEchosReceived = 0;
//                newWakeup = true;
//                //wakeupNeighbours(); // problem?
//            }
//
//        } else {
//            // first wakeup
//            System.out.println(this + " woken up by " + neighbour + " rank: " + neighourRank + " number of messages received: " + ++numberOfMessagesReceived + " thread: " + currentThread().getName());
////            ++numberOfMessagesReceived;
//            neighbourAwakenMe = neighbour;
//            this.rank = neighourRank;
//            sleeping = false;
//        }
//
//        notifyAll();
//    }
//
//    @Override
//    public synchronized void echo(Node neighbour, int neighbourRank, Object data) {
//
//        ++numberOfEchosReceived;
//        spanningTreeNode.addPrecursor((SpanningTreeNode) data);
//        System.out.println(this + " received an echo from " + neighbour + " rank: " + neighbourRank + " number of echos received: " + numberOfEchosReceived + " thread: " + currentThread().getName());
//        newEcho = true;
//        notifyAll();
//    }
}
