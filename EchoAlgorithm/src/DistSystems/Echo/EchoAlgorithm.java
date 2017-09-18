package DistSystems.Echo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by Hendrik Mahrt on 17.09.2017.
 */
public class EchoAlgorithm {

    LinkedList<NodeImp> nodes = new LinkedList<>();
    CyclicBarrier helloBarrier = new CyclicBarrier(8);

    public static void main(String[] args) {

        for (int i = 0; i < 1000; i++) {
            EchoAlgorithm echoAlgorithm = new EchoAlgorithm();
            echoAlgorithm.start();
        }
    }

    public EchoAlgorithm() {
        setupNeighbours();
    }

    public void start() {
        startAllNodes();
        joinAllNodes();
        //printAllNodesNeighbours();
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

    private void setupRandomNeighbours() {

    }

    private void setupNeighbours() {
        nodes.add(new NodeImp("0", true, helloBarrier));
        nodes.add(new NodeImp("1", false, helloBarrier));
        nodes.add(new NodeImp("2", false, helloBarrier));
        nodes.add(new NodeImp("3", false, helloBarrier));
        nodes.add(new NodeImp("4", false, helloBarrier));
        nodes.add(new NodeImp("5", false, helloBarrier));
        nodes.add(new NodeImp("6", false, helloBarrier));
        nodes.add(new NodeImp("7", false, helloBarrier));

        nodes.get(0).setupNeighbours(nodes.get(1), nodes.get(2));
        nodes.get(2).setupNeighbours(nodes.get(3), nodes.get(1));
        nodes.get(3).setupNeighbours(nodes.get(4), nodes.get(5), nodes.get(3));
        nodes.get(5).setupNeighbours(nodes.get(0));
        nodes.get(6).setupNeighbours(nodes.get(1), nodes.get(4));
        nodes.get(7).setupNeighbours(nodes.get(6));
        nodes.get(7).setupNeighbours(nodes.get(1));
    }

    private void setupNeighboursJSON() {
        JSONObject nodesJSON = new JSONObject("");
        JSONArray nodesJSONArray = nodesJSON.getJSONArray("nodes");

        nodesJSONArray.forEach((nodeObj) -> {
            JSONObject node = (JSONObject) nodeObj;
            node.get("name");
            node.getBoolean("initiator");
            node.getJSONArray("neighbours");
        });
    }
}
