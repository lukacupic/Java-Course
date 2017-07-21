package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * A node of the document model which represents a textual element.
 *
 * @author Luka Čupić
 */
public class TextNode extends Node {

    /**
     * The textual value of the node.
     */
    private String text;

    /**
     * The default constructor.
     *
     * @param text the value of this node.
     */
    public TextNode(String text) {
        this.text = text;
    }

    /**
     * Returns the textual value of the node.
     *
     * @return the textual value of the node.
     */
    public String getText() {
        return text;
    }
}
