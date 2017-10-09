package DistSystems.Utilities;

import DistSystems.Interfaces.NodeAbstract;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.Stream;

/**
 * Parses a file and constructs a list of nodes and a neighbourhood.
 * Created by Michael Guenster, Andre Schriever, Hendrik Mahrt on 09.10.2017.
 */
public class GraphFileParser {
    private String filename;
    private CyclicBarrier helloBarrier;
    private NodeFactory nodeFactory;
    private List<NodeAbstract> nodes;

    public GraphFileParser(Type nodeType, String filename) {
        this.filename = filename;

        nodeFactory = new NodeFactory(nodeType);
        nodes = new ArrayList<>();
    }

    /**
     * Parses the file, and returns a list of Nodes with
     * setup neighbourhood.
     * @return List of Nodes.
     * @throws IOException in case file can not be read.
     */
    public List<NodeAbstract> parse() throws IOException {
        // check file existing
        File file = new File(filename);

        if (!file.exists())
            throw new FileNotFoundException("Graph definition file not found");

        Stream<String> lines = getLines(file);

        int numberOfNodes = (int)lines.count();
        helloBarrier = new CyclicBarrier(numberOfNodes);

        lines = getLines(file);
        lines.forEach((String node) -> createNode(node));

        lines = getLines(file);
        lines.forEach((String node) -> generateNeighbourhood(node));

        return nodes;
    }

    private void generateNeighbourhood(String nodeDefinition) {
        String[] components = nodeDefinition.split(":");

        NodeAbstract node = null;
        try {
            node = getNode(components[0]);
        } catch (InstantiationException e) {
            return;
        }

        if (components.length < 3)
            return;

        String neighboursString = components[2];
        String[] neighbourIds = neighboursString.split(",");

        LinkedList<NodeAbstract> neighbours = new LinkedList<>();

        for (String neigbour : neighbourIds) {
            NodeAbstract neighbour = nodes.get(Integer.parseInt(neigbour));
            if (neigbour == null)
                continue;
            neighbours.add(neighbour);
        }

        node.setupNeighbours(neighbours.toArray(new NodeAbstract[neighbours.size()]));
    }

    private NodeAbstract getNode(String identifier) throws InstantiationException {
        int nodeId = Integer.parseInt(identifier);
        NodeAbstract node = nodes.get(nodeId);

        if (node == null)
            throw new InstantiationException("Node not found");

        return node;
    }

    private Stream<String> getLines(File file) throws FileNotFoundException {
        FileReader fileReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileReader);

        return reader.lines();
    }

    private void createNode(String nodeDefinition) {
        String[] components = nodeDefinition.split(":");

        int id = Integer.parseInt(components[0]);
        String name = components[0];
        boolean initiator = false;
        if (components[1].equals("t"))
            initiator = true;

        NodeAbstract node = nodeFactory.Create(id, name, initiator, helloBarrier);

        nodes.add(node);
    }
}
