package DistSystems.Interfaces;

/**
 * This Interface defines the messages for Election algorithm.
 */
public interface ElectionNode {
    /**
     * Explores or conquers the node.
     * @param neighbour Neighbour, who sent the message.
     * @param neighboursRank Current rank of the neighbour.
     */
    void leaderExplore(ElectionNode neighbour, int neighboursRank);

    /**
     * Echo message propagating back. Contains the highest rank in the
     * graph and informs the last initiator, that he won.
     * @param neighbour Neighbour, who sent the message.
     * @param neighboursRank Current rank of the neighbour.
     */
    void leaderEcho(ElectionNode neighbour, int neighboursRank);

    /**
     * Message to inform the node, who is the winner and that echo will
     * start afterwards.
     * @param neighbour Neighbour, who sent the message.
     * @param winner Winner node.
     */
    void leaderWinner(ElectionNode neighbour, ElectionNode winner);
}
