package hr.fer.zemris.bf.model;

import java.util.List;
import java.util.function.BinaryOperator;

/**
 * Represents a binary operator which performs an operation upon two
 * or more {@link Node} objects.
 *
 * @author Luka Čupić
 */
public class BinaryOperatorNode implements Node {

    /**
     * Represents this operator's name.
     */
    private String name;

    /**
     * Represents the children of this operator.
     */
    private List<Node> children;

    /**
     * Represents the operator object who will perform the operation upon
     * the {@link BinaryOperatorNode#children} object.
     */
    private BinaryOperator<Boolean> operator;

    /**
     * Creates a new instance of this class.
     *
     * @param name     the name of the binary operator
     * @param children the list of children on which the binary operation will be performed
     * @param operator the operator do delegate the operation to
     */
    public BinaryOperatorNode(String name, List<Node> children, BinaryOperator<Boolean> operator) {
        this.name = name;
        this.children = children;
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
     * Returns the children of this operator.
     *
     * @return the list of this operator's children nodes
     */
    public List<Node> getChildren() {
        return children;
    }

    /**
     * Returns the actual operator object of this binary operator.
     *
     * @return the object which performs the operation.
     */
    public BinaryOperator<Boolean> getOperator() {
        return operator;
    }
}
