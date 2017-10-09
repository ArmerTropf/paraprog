package DistSystems.Utilities;

import DistSystems.Echo.EchoNode;
import DistSystems.Election.ElectionNodeImp;
import DistSystems.Interfaces.NodeAbstract;

import java.lang.reflect.Type;
import java.util.concurrent.CyclicBarrier;

/**
 * Creates nodes with the type, passed in constructor.
 * Created by Michael Guenster, Andre Schriever, Hendrik Mahrt on 09.10.2017.
 */
class NodeFactory {

    private Type nodeType;
    NodeFactory(Type nodeType) {
        this.nodeType = nodeType;
    }

    /**
     * Creates a Node with the type of the factory.
     * @param id ID of the node.
     * @param name Name of the node.
     * @param initiator If this node is an initiator.
     * @param helloBarrier CyclicBarrier for synchronising start.
     * @return Instance of Node.
     */
    NodeAbstract Create(int id, String name, boolean initiator, CyclicBarrier helloBarrier) {
        if (nodeType == EchoNode.class)
            return new EchoNode(name, initiator, helloBarrier);
        else if (nodeType == ElectionNodeImp.class)
            return new ElectionNodeImp(name, id, initiator, helloBarrier);

        throw new TypeNotPresentException("Type not supported", null);
    }
}
