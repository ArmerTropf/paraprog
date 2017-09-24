package DistSystems.Election;

import DistSystems.Echo.EchoAlgorithm;
import DistSystems.Utilities.RandomGraphBuilder;

import java.util.LinkedList;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by Hendrik Mahrt on 23.09.17.
 */
public class ElectionAlgorithm extends EchoAlgorithm {

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            ElectionAlgorithm electionAlgorithm = new ElectionAlgorithm();
            electionAlgorithm.start();
        }
    }

    public ElectionAlgorithm() {

//        generateGraph();

        RandomGraphBuilder graphBuilder = new RandomGraphBuilder(10, 3, 0.3);
        nodes = graphBuilder.build();
    }

    private void generateGraph() {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(6);
        nodes = new LinkedList<>();
        nodes.add(new ElectionNode("0", 0, true, cyclicBarrier));
        nodes.add(new ElectionNode("1", 1, false, cyclicBarrier));
        nodes.add(new ElectionNode("2", 2, false, cyclicBarrier));
        nodes.add(new ElectionNode("3", 3, false, cyclicBarrier));
        nodes.add(new ElectionNode("4", 4, false, cyclicBarrier));
        nodes.add(new ElectionNode("5", 5, true, cyclicBarrier));

        nodes.get(0).setupNeighbours(nodes.get(1), nodes.get(2));
        nodes.get(1).setupNeighbours(nodes.get(2));
        nodes.get(2).setupNeighbours(nodes.get(3));
        nodes.get(3).setupNeighbours(nodes.get(4), nodes.get(5));
//        nodes.get(4).setupNeighbours();
//        nodes.get(5).setupNeighbours();

    }
}
