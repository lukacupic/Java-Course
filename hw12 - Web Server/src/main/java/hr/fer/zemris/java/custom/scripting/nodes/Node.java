package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.ArrayList;
import java.util.List;

/**
 * A generic node of the document model from which all nodes are
 * inherited from.
 *
 * @author Luka Čupić
 */
public abstract class Node {

    /**
     * The collection of child nodes.
     */
    private List<Node> nodes;

    /**
     * The default constructor.
     */
    public Node() {
        super();
    }

    /**
     * Performs a visit on this object by the specified {@link INodeVisitor}.
     *
     * @param visitor the node which is to be visited
     */
    public abstract void accept(INodeVisitor visitor);

    /**
     * Adds a child to this node.
     *
     * @param child the child node to be added.
     */
    public void addChildNode(Node child) {
        if (nodes == null) {
            nodes = new ArrayList<>();
        }
        nodes.add(child);
    }

    /**
     * Returns the number of children of this node.
     *
     * @return the number of children.
     */
    public int numberOfChildren() {
        return nodes != null ? nodes.size() : 0;
    }

    /**
     * Returns a child node from the position {@code index}.
     *
     * @param index the position to return the child from.
     * @return a child node from the position {@code index}.
     */
    public Node getChild(int index) {
        return nodes.get(index);
    }
}
