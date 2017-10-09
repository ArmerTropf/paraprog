package DistSystems.Utilities;

import DistSystems.Interfaces.NodeAbstract;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by Michael Guenster, Andre Schriever, Hendrik Mahrt on 23.09.2017.
 *
 * Generates random graphs with configurable properties.
 */
public class RandomGraphBuilder {

    private final NodeFactory nodeFactory;
    private List<NodeAbstract> nodes;
    private CyclicBarrier helloBarrier;

    private int numberOfNodes;
    private int numberOfInitiators;
    private double density;

    private int numberOfInitiatorsCreated = 0;
    private boolean allowSlings = false;
    private boolean allowCircles = false;

    public RandomGraphBuilder(Type nodeType, int numberOfNodes, int numberOfInitiators) {
        this.numberOfNodes = numberOfNodes;
        this.numberOfInitiators = numberOfInitiators;
        this.density = 0.5;

        this.nodeFactory = new NodeFactory(nodeType);
        this.nodes = new LinkedList<>();
        this.helloBarrier = new CyclicBarrier(numberOfNodes);
    }

    /**
     * Allows the occurrence of slings in the
     * randomly generated graph.
     */
    public void allowSlings() {
        allowSlings = true;
    }

    /**
     * Allows the occurrence of circles in the
     * randomly generated graph.
     */
    public void allowCircles() {
        allowCircles = true;
    }

    /**
     * Generates a complete graph.
     */
    public void completeGraph() {
        allowSlings = true;
        density = 1;
    }

    /**
     * Builds the random graph.
     * @return List of nodes.
     */
    public List<NodeAbstract> build() {
        generateGraph();
        return nodes;
    }

    private void generateGraph() {
        generateNodes();
        generateNeighbourhood();
    }

    private void generateNodes() {
        for (int i = 0; i < numberOfNodes; i++) {
            NodeAbstract node = nodeFactory.Create(
                    i,
                    Integer.toString(i),
                    isNodeAnInitiator(),
                    helloBarrier);
            nodes.add(node);
        }
    }

    private void generateNeighbourhood() {
        // TODO: prevent circles if allowCircles == false
        for (NodeAbstract node : nodes) {
            LinkedList<NodeAbstract> neighbours = new LinkedList<>();

            for (NodeAbstract neighbour : nodes) {
                if (!allowSlings && neighbour == node)
                    continue;

                if (Math.random() < density)
                    neighbours.add(neighbour);
            }

            node.setupNeighbours(neighbours.toArray(new NodeAbstract[neighbours.size()]));
        }
    }

    private boolean isNodeAnInitiator() {
        if (numberOfInitiators == numberOfInitiatorsCreated)
            return false;

        if (Math.random() <= chanceOfBecomingAnInitiator()) {
            ++numberOfInitiatorsCreated;
            return true;
        }

        return false;
    }

    private double chanceOfBecomingAnInitiator() {
        return (double) numberOfInitiators / (double) numberOfNodes;
    }

    public void setDensity(double density) {
        this.density = density;
    }
}
