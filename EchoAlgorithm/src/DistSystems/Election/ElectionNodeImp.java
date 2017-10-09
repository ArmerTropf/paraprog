package DistSystems.Election;

import DistSystems.Echo.EchoNode;
import DistSystems.Interfaces.ElectionNode;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;

/**
 * Election Node implementation.
 * Perfoms the echo/election algorithm and determines the leader which perfoms the normal
 * echo algorithm afterwards.
 * Created by Michael Guenster, Andre Schriever, Hendrik Mahrt on 08.10.2017.
 */
public class ElectionNodeImp extends EchoNode implements ElectionNode {
    // states
    private enum states {
        ASLEEP,
        EXPLORED,
        SENT_EXPLORERS,
        CONQUERED,
        ALL_MESSAGES_RECEIVED,
        VICTORY_MESSAGE_RECEIVED,
        READY_FOR_ECHO
    }

    private int currentRank;
    private ElectionNode exploredByNeighbour = null;
    private ConcurrentHashMap<Integer, Integer> numberOfMessagesReceived = new ConcurrentHashMap<>();
    private ElectionNode electedLeader = null;
    private ElectionNode victoryByNeighbour = null;

    private states state = states.ASLEEP;

    public ElectionNodeImp(String name, int rank, boolean initiator, CyclicBarrier barrier) {
        super(name, false, barrier);
        currentRank = rank;

        // set state of initiator, so that he sends explorers right away
        if (initiator)
            state = states.EXPLORED;
    }

    @Override
    public void run() {
        try {
            sendHelloToAllNeighbours();

            waitForExplorer();

            while (state != states.VICTORY_MESSAGE_RECEIVED) {
                sendExplorers();

                if (state == states.EXPLORED)
                    state = states.SENT_EXPLORERS;

                while (!waitForAllMessages()) {
                    sendExplorers();
                    state = states.SENT_EXPLORERS;
                }

                sendLeaderEcho();

                // we need to go back to wait for messages from here
                waitForVictoryMessageOrBeingConquered();
            }

            sendVictoryMessages();
            state = states.READY_FOR_ECHO;

            System.out.println(this + "["+state+" mesg:"+numberOfMessagesReceived+"/"+neighbours.size()+"]: finished. Waiting for echo!");

            super.run();

        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    private synchronized boolean waitForVictoryMessageOrBeingConquered() throws InterruptedException {
        while (state != states.VICTORY_MESSAGE_RECEIVED && state != states.CONQUERED) {
            wait();
        }

        if (state == states.VICTORY_MESSAGE_RECEIVED)
            System.out.println(this + "["+state+" mesg:"+numberOfMessagesReceived+"/"+neighbours.size()+"]: received victory message");
        else {
            System.out.println(this + "["+state+" mesg:"+numberOfMessagesReceived+"/"+neighbours.size()+"]: conquered after leader echo");
            return false;
        }
        return true;
    }

    private synchronized void sendLeaderEcho() {
        if (exploredByNeighbour == null) {
            System.out.println(this + "[" + state + " mesg:" + numberOfMessagesReceived + "/" + neighbours.size() + "]: im leader!");
            electedLeader = this;
            initiator = true;
            state = states.VICTORY_MESSAGE_RECEIVED;
            return;
        }
        System.out.println(this + "[" + state + " mesg:" + numberOfMessagesReceived + "/" + neighbours.size() + "]: sending leader echo");
        if (state == states.ALL_MESSAGES_RECEIVED)
        {
            exploredByNeighbour.leaderEcho(this, this.currentRank);
        }
    }

    private void sendVictoryMessages() {
        System.out.println(this + "["+state+" mesg:"+numberOfMessagesReceived+"/"+neighbours.size()+"]: sending victory messages");
        neighbours
                .parallelStream()
                .filter((neighbour) -> !neighbour.equals(victoryByNeighbour))
                .forEach((neighbour) -> ((ElectionNode)neighbour).leaderWinner(this, electedLeader));
    }

    private synchronized boolean waitForAllMessages() throws InterruptedException {
        while (!(numberOfMessagesReceived.get(currentRank) != null && numberOfMessagesReceived.get(currentRank) >= neighbours.size())) {

            if (state == states.CONQUERED) {
                System.out.println(this + "["+state+" mesg:"+numberOfMessagesReceived+"/"+neighbours.size()+"]: resending explorers after being conquered");
                return false;
            }
            wait();
        }
        System.out.println(this + "["+state+" mesg:"+numberOfMessagesReceived+"/"+neighbours.size()+"]: received all messages");

        if (state == states.CONQUERED) {
            System.out.println(this + "["+state+" mesg:"+numberOfMessagesReceived+"/"+neighbours.size()+"]: resending explorers after being conquered");
            return false;
        }

        state = states.ALL_MESSAGES_RECEIVED;
        return true;
    }

    private void sendExplorers() {
        // local copies of state information. Prevents sending of messages with newer rank.
        int localCurrentRank = currentRank;
        ElectionNode localExploredByNeighbour = exploredByNeighbour;
        neighbours
                .parallelStream()
                .filter((neighbour) -> !neighbour.equals(localExploredByNeighbour))
                .forEach((neighbour) -> ((ElectionNode)neighbour).leaderExplore(this, localCurrentRank));
    }

    private synchronized void waitForExplorer() throws InterruptedException {
        while (state != states.EXPLORED) {
            wait(1000);

            if (state != states.EXPLORED) {
                // not explored after timeout. lets be initiator ourself
                state = states.EXPLORED;
            }
        }
    }

    @Override
    public synchronized void leaderExplore(ElectionNode neighbour, int neighboursRank) {

        if (state == states.ASLEEP) {
            numberOfMessagesReceived.put(neighboursRank, 1);
            System.out.println(this + "["+state+" mesg:"+numberOfMessagesReceived+"/"+neighbours.size()+"]: explored by " + neighbour + " rank: " + neighboursRank);
            // if (currentRank < neighboursRank) { // commented out, because it caused more problems than it fixes
                // this if statement is necessary for a higher node to participate in a leader election
                // but it causes locks in other stages...
                currentRank = neighboursRank;
                exploredByNeighbour = neighbour;
            // }
            state = states.EXPLORED;
        } else {
            if (currentRank < neighboursRank) {
                numberOfMessagesReceived.put(neighboursRank, 1);
                System.out.println(this + "["+state+" mesg:"+numberOfMessagesReceived+"/"+neighbours.size()+"]: conquered by " + neighbour + " rank: " + neighboursRank);
                currentRank = neighboursRank;
                exploredByNeighbour = neighbour;
                state = states.CONQUERED;
            } else if (currentRank == neighboursRank) {
                numberOfMessagesReceived.put(neighboursRank, numberOfMessagesReceived.getOrDefault(neighboursRank, 0) + 1);
                System.out.println(this + "["+state+" mesg:"+numberOfMessagesReceived+"/"+neighbours.size()+"]: explored by " + neighbour + " with same rank: " + neighboursRank);
            } else {
                System.out.println(this + "["+state+" mesg:"+numberOfMessagesReceived+"/"+neighbours.size()+"]: explored by " + neighbour + " with lower rank: " + neighboursRank + ". ignoring!");
            }
        }
        notifyAll();
    }

    @Override
    public synchronized void leaderEcho(ElectionNode neighbour, int neighboursRank) {
        if (neighboursRank == currentRank) {
            numberOfMessagesReceived.put(neighboursRank, numberOfMessagesReceived.getOrDefault(neighboursRank, 0) + 1);
            System.out.println(this + "["+state+" mesg:"+numberOfMessagesReceived+"/"+neighbours.size()+"]: got echo from " + neighbour + " with rank: " + neighboursRank);
        }
        notifyAll();
    }

    @Override
    public synchronized void leaderWinner(ElectionNode neighbour, ElectionNode winner) {
        System.out.println(this + "["+state+" mesg:"+numberOfMessagesReceived+"/"+neighbours.size()+"]: got victory message from " + neighbour + " with leader: " + winner);
        victoryByNeighbour = neighbour;
        electedLeader = winner;
        state = states.VICTORY_MESSAGE_RECEIVED;
        notifyAll();
    }
}
