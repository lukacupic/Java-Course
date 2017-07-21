package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;

/**
 * A node of the document model which represents a command which generates textual output.
 *
 * @author Luka Čupić
 */
public class EchoNode extends Node {

    /**
     * A collection of elements.
     */
    private Element[] elements;

    /**
     * Instantiates a new EchoNode object from the passed elements.
     *
     * @param elements the elements to add to this node.
     */
    public EchoNode(Element[] elements) {
        if (elements == null) {
            throw new IllegalArgumentException();
        }

        this.elements = elements;
    }

    /**
     * Returns an array of elements of this node.
     *
     * @return an array of elements of this node.
     */
    public Element[] getElements() {
        return elements;
    }

    @Override
    public void accept(INodeVisitor visitor) {
        visitor.visitEchoNode(this);
    }
}
