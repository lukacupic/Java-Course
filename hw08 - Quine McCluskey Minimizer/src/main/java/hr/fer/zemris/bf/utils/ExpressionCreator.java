package hr.fer.zemris.bf.utils;

import hr.fer.zemris.bf.model.*;

import java.util.List;

/**
 * Creates a new expression of a boolean function represented
 * by it's tree structure, which is modeled bya {@link Node}
 * object.
 *
 * @author Luka Čupić
 */
public class ExpressionCreator implements NodeVisitor {

    /**
     * Represents the expression builder.
     */
    private StringBuilder expression;

    /**
     * Creates a new class instance.
     */
    public ExpressionCreator() {
        expression = new StringBuilder();
    }

    @Override
    public void visit(ConstantNode node) {
        expression.append(node.getValue()).append(" ");
    }

    @Override
    public void visit(VariableNode node) {
        expression.append(node.getName()).append(" ");
    }

    @Override
    public void visit(UnaryOperatorNode node) {
        expression.append(node.getName()).append(" ");
        Util.visitChild(this, node.getChild());
    }

    @Override
    public void visit(BinaryOperatorNode node) {
        List<Node> children = node.getChildren();

        Node first = children.get(0);
        Util.visitChild(this, first);

        for (int i = 1; i < children.size(); i++) {
            expression.append(node.getName()).append(" ");

            Node child = children.get(i);
            Util.visitChild(this, child);
        }
    }

    /**
     * Gets the created expression.
     *
     * @return the created expression.
     */
    public String getExpression() {
        String str = expression.toString();
        expression.setLength(0);

        return str.toUpperCase().trim();
    }
}