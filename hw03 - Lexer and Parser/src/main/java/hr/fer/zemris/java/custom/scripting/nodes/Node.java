package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;

/**
 * A generic node of the document model from which all nodes are inherited from.
 *
 * @author Luka Čupić
 */
public class Node {

    /**
     * The collection of child nodes.
     */
    ArrayIndexedCollection nodes;

    /**
     * The default constructor.
     */
    public Node() {
        super();
    }

    /**
     * Adds a child to this node.
     *
     * @param child the child node to be added.
     */
    public void addChildNode(Node child) {
        if (nodes == null) {
            nodes = new ArrayIndexedCollection();
        }

        nodes.add(child);
    }

    /**
     * Returns the number of children of this node.
     *
     * @return the number of children.
     */
    public int numberOfChildren() {
        if (nodes == null) {
            return 0;
        }

        return nodes.size();
    }

    /**
     * Returns a child node from the position {@code index}.
     *
     * @param index the position to return the child from.
     * @return a child node from the position {@code index}.
     */
    public Node getChild(int index) {
        return (Node) nodes.get(index);
    }
}
