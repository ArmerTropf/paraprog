package DistSystems.Interfaces;

/**
 * 
 */
public interface ElectionNode {
    
    // leaderExplore - send 'wakeup' to neighbours with your rank/rank of the initator that woke you up
    
    /**
     * 
     */
    public void leaderExplore(ElectionNode neighbour, int neighboursRank);
    
    // leaderEcho - send back echo to heighest ranked neighbour who woke you up
    
    /**
     * 
     */
    public void leaderEcho(ElectionNode neighbour, int neighboursRank);
    
}
