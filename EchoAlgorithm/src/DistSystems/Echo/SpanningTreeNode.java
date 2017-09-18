package DistSystems.Echo;

import DistSystems.Interfaces.Node;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Hendrik Mahrt on 18.09.2017.
 */
public class SpanningTreeNode {

    private Node node;

    private Set<SpanningTreeNode> precursors = new HashSet<SpanningTreeNode>();

    public SpanningTreeNode(Node node) {
        this.node = node;
    }

    public void addPrecursor(SpanningTreeNode precursor) {
        precursors.add(precursor);
    }

    @Override
    public String toString() {
        String precursorString = "";
        for (SpanningTreeNode precursor : precursors) {
            precursorString+= precursor + "\t";
        }
        return node + " | \t" + precursorString;
    }
}
