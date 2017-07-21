package hr.fer.zemris.bf.model;

import java.util.function.UnaryOperator;

/**
 * Represents a unary operator which performs an operation upon a
 * single {@link Node} object.
 *
 * @author Luka Čupić
 */
public class UnaryOperatorNode implements Node {

    /**
     * Represents this operator's name.
     */
    private String name;

    /**
     * Represents the child of this operator.
     */
    private Node child;

    /**
     * Represents the operator object who will perform the operation upon
     * the {@link UnaryOperatorNode#child} object.
     */
    private UnaryOperator<Boolean> operator;

    /**
     * Creates a new instance of this class.
     *
     * @param name     the name of the unary operator
     * @param child    a child on which the unary operation will be performed
     * @param operator the operator do delegate the operation to
     */
    public UnaryOperatorNode(String name, Node child, UnaryOperator<Boolean> operator) {
        this.name = name;
        this.child = child;
        this.operator = operator;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Gets the name of this operator.
     *
     * @return this operators' name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the child of this operator.
     *
     * @return this operator's child
     */
    public Node getChild() {
        return child;
    }

    /**
     * Returns the actual operator object of this unary operator.
     *
     * @return the object which performs the operation.
     */
    public UnaryOperator<Boolean> getOperator() {
        return operator;
    }
}
