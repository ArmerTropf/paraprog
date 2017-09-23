package DistSystems.Echo;

import DistSystems.Utilities.RandomGraphBuilder;

import java.util.List;

/**
 * Created by Hendrik Mahrt on 17.09.2017.
 */
public class EchoAlgorithm {

    List<NodeImp> nodes;

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            EchoAlgorithm echoAlgorithm = new EchoAlgorithm();
            echoAlgorithm.start();
        }
    }

    public EchoAlgorithm() {
        RandomGraphBuilder graphBuilder = new RandomGraphBuilder(10, 1, 1);
        nodes = graphBuilder.build();
    }

    public void start() {
        printAllNodesNeighbours();
        startAllNodes();
        joinAllNodes();
    }

    private void startAllNodes() {
        nodes.forEach(node -> node.start());
    }

    private void joinAllNodes() {
        try {
            for (NodeImp node : nodes) {
                node.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void printAllNodesNeighbours() {
        nodes.forEach(node -> node.printNeighbours());
    }
}
