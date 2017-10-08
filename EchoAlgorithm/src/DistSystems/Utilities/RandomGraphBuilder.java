package DistSystems.Utilities;

import DistSystems.Echo.EchoNode;
import DistSystems.Election.ElectionNodeImp;
import DistSystems.Election.ElectionNodeStates;
import DistSystems.Interfaces.Node;
import DistSystems.Interfaces.NodeAbstract;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by Hendrik Mahrt on 23.09.2017.
 *
 * Generates random graphs with configurable properties.
 */
public class RandomGraphBuilder {

    private List<ElectionNodeStates> nodes;
    private CyclicBarrier helloBarrier;

    private int numberOfNodes;
    private int numberOfInitiators;
    private double density;

    private int numberOfInitiatorsCreated = 0;

    public RandomGraphBuilder(int numberOfNodes, int numberOfInitiators, double density) {
        this.numberOfNodes = numberOfNodes;
        this.numberOfInitiators = numberOfInitiators;
        this.density = density;

        this.nodes = new LinkedList<>();
        this.helloBarrier = new CyclicBarrier(numberOfNodes);
    }

    public List<ElectionNodeStates> build() {
        generateGraph();
        return nodes;
    }

    private void generateGraph() {
        generateNodes();
        generateNeighbourhood();
    }

    private void generateNodes() {
        for (int i = 0; i < numberOfNodes; i++) {
            ElectionNodeStates node = new ElectionNodeStates(
                    Integer.toString(i),
                    i,
                    isNodeAnInitiator(),
                    helloBarrier);
            nodes.add(node);
        }
    }

    private void generateNeighbourhood() {
        // TODO: generate tree
        // TODO: generate circle

        for (NodeAbstract node : nodes) {
            LinkedList<NodeAbstract> neighbours = new LinkedList<>();

            for (NodeAbstract neighbour : nodes) {
                // TODO: switch for slings
                if (neighbour == node)
                    continue;

                // TODO: cross checking with neighbour: edge already existing?
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
}
