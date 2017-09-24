package DistSystems.Echo;

import DistSystems.Election.ElectionAlgorithm;
import DistSystems.Election.ElectionNode;
import DistSystems.Interfaces.NodeAbstract;
import DistSystems.Utilities.RandomGraphBuilder;

import java.util.List;

/**
 * Created by Hendrik Mahrt on 17.09.2017.
 */
public class EchoAlgorithm {

    protected List<ElectionNode> nodes;

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            EchoAlgorithm echoAlgorithm = new EchoAlgorithm();
            echoAlgorithm.start();
        }
    }

    public EchoAlgorithm() {
        RandomGraphBuilder graphBuilder = new RandomGraphBuilder(10, 1, 0.2);
        //nodes = graphBuilder.build();
    }

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
            for (NodeAbstract node : nodes) {
                node.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void printAllNodesNeighbours() {
        nodes.forEach(ElectionNode::printNeighbours);
    }
}
