package hr.fer.zemris.bf.model;

/**
 * Represents a node which holds a variable.
 *
 * @author Luka Čupić
 */
public class VariableNode implements Node {

    /**
     * The name of variable which is represented by this node.
     */
    private String name;

    /**
     * Creates a new variable node of the given name.
     *
     * @param name the name of the variable
     */
    public VariableNode(String name) {
        this.name = name;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Gets the name of this node.
     *
     * @return this node's name
     */
    public String getName() {
        return name;
    }
}
