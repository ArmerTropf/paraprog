package DistSystems.Interfaces;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Hendrik Mahrt on 18.09.2017.
 *
 * Spanning tree node.
 */
public class SpanningTreeNode {

    private Node node;

    private Set<SpanningTreeNode> precursors = new HashSet<>();

    public SpanningTreeNode(Node node) {
        this.node = node;
    }

    public void addPrecursor(SpanningTreeNode precursor) {
        precursors.add(precursor);
    }

    @Override
    public String toString() {
        return toString(0);
    }

    public String toString(int indent) {
        String treeString = "";

        for (int i = 0; i < indent; i++) {
            treeString+="\t";
        }

        treeString+= "" + node.toString() + "\n";

        for (SpanningTreeNode precursor : precursors) {
            treeString+= precursor.toString(indent + 1);
        }

        return treeString;
    }
}
