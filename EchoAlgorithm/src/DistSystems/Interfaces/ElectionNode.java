package DistSystems.Interfaces;

/**
 * 
 */
public interface ElectionNode {
    

    void leaderExplore(ElectionNode neighbour, int neighboursRank);

    void leaderEcho(ElectionNode neighbour, int neighboursRank);

    void leaderVictory(ElectionNode neighbour, ElectionNode victor);
}
