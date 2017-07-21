package hr.fer.zemris.bf.model;

/**
 * Represents a tree model node from which all nodes are inherited from.
 *
 * @author Luka Čupić
 */
public interface Node {

    /**
     * Performs a visit on the given {@link NodeVisitor} object.
     *
     * @param visitor the node which is to be visited
     */
    void accept(NodeVisitor visitor);
}
