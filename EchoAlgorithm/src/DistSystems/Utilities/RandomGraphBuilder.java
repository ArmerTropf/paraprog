package DistSystems.Utilities;

import DistSystems.Echo.NodeImp;
import DistSystems.Interfaces.Node;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by Hendrik Mahrt on 23.09.2017.
 *
 * Generates random graphs with configurable properties.
 */
public class RandomGraphBuilder {

    private List<NodeImp> nodes;
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

    public List<NodeImp> build() {
        generateGraph();
        return nodes;
    }

    private void generateGraph() {
        generateNodes();
        generateNeighbourhood();
    }

    private void generateNodes() {
        for (int i = 0; i < numberOfNodes; i++) {
            NodeImp node = new NodeImp(
                    Integer.toString(i),
                    isNodeAnInitiator(),
                    helloBarrier);
            nodes.add(node);
        }
    }

    private void generateNeighbourhood() {
        // TODO: generate tree
        // TODO: generate circle

        for (NodeImp node : nodes) {
            LinkedList<Node> neighbours = new LinkedList<>();

            for (NodeImp neighbour : nodes) {
                // TODO: switch for slings
                if (neighbour == node)
                    continue;

                // TODO: cross checking with neighbour: edge already existing?
                if (Math.random() < density)
                    neighbours.add(neighbour);
            }

            node.setupNeighbours(neighbours.toArray(new Node[neighbours.size()]));
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
