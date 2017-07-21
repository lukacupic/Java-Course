package hr.fer.zemris.bf.model;

/**
 * Represents a node which holds a constant boolean value, being
 * either true or false.
 *
 * @author Luka Čupić
 */
public class ConstantNode implements Node {

    /**
     * Represents the boolean value of this node
     */
    private boolean value;

    /**
     * Creates a new instance of this class.
     *
     * @param value the value of this node
     */
    public ConstantNode(boolean value) {
        this.value = value;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Gets the value of this node.
     *
     * @return this node's value
     */
    public boolean getValue() {
        return value;
    }
}
