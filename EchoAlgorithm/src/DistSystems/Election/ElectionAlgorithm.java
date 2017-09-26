package DistSystems.Election;

import DistSystems.Echo.EchoAlgorithm;
import DistSystems.Echo.EchoNode;
import DistSystems.Interfaces.ElectionNode;
import DistSystems.Interfaces.NodeAbstract;
import DistSystems.Utilities.RandomGraphBuilder;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by Hendrik Mahrt on 23.09.17.
 */
public class ElectionAlgorithm {

    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
            ElectionAlgorithm electionAlgorithm = new ElectionAlgorithm();
            electionAlgorithm.start();
        }
    }

    public ElectionAlgorithm() {

        generateGraph();

//        RandomGraphBuilder graphBuilder = new RandomGraphBuilder(10, 3, 0.3);
//        nodes = graphBuilder.build();
    }

    protected List<ElectionNodeImp> nodes;

    public void start() {
        printAllNodesNeighbours();
        startAllNodes();
        joinAllNodes();
    }

    private void startAllNodes() {
        nodes.forEach(Thread::start);
    }

    private void joinAllNodes() {
        try {
            for (ElectionNodeImp node : nodes) {
                node.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void printAllNodesNeighbours() {
        nodes.forEach(ElectionNodeImp::printNeighbours);
    }

    private void generateGraph() {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(6);
        nodes = new LinkedList<>();
        nodes.add(new ElectionNodeImp("0", 0, true, cyclicBarrier));
        nodes.add(new ElectionNodeImp("1", 1, false, cyclicBarrier));
        nodes.add(new ElectionNodeImp("2", 2, false, cyclicBarrier));
        nodes.add(new ElectionNodeImp("3", 3, false, cyclicBarrier));
        nodes.add(new ElectionNodeImp("4", 4, false, cyclicBarrier));
        nodes.add(new ElectionNodeImp("5", 5, true, cyclicBarrier));

        nodes.get(0).setupNeighbours(nodes.get(1), nodes.get(2));
        nodes.get(1).setupNeighbours(nodes.get(2));
        nodes.get(2).setupNeighbours(nodes.get(3));
        nodes.get(3).setupNeighbours(nodes.get(4), nodes.get(5));
//        nodes.get(4).setupNeighbours();
//        nodes.get(5).setupNeighbours();

    }
}
