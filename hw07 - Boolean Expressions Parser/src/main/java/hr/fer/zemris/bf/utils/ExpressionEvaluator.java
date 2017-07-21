package hr.fer.zemris.bf.utils;

import hr.fer.zemris.bf.model.*;
import hr.fer.zemris.bf.parser.Parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Represents a node visitor which evaluates the expression given
 * to the {@link Parser} object.
 *
 * @author Luka Čupić
 */
public class ExpressionEvaluator implements NodeVisitor {

    /**
     * An array of boolean values which represents values of
     * the given variables.
     */
    private boolean[] values;

    /**
     * Maps variable names to their corresponding positions.
     */
    private Map<String, Integer> positions;

    /**
     * The stack to store the expression values on.
     */
    private Stack<Boolean> stack;

    /**
     * Creates a new instance of this class.
     */
    public ExpressionEvaluator(List<String> variables) {
        stack = new Stack<>();
        positions = new HashMap<>();

        for (int i = 0; i < variables.size(); i++) {
            String var = variables.get(i);
            positions.put(var, i);
        }
    }

    /**
     * Sets the values of the variables.
     *
     * @param values the values to set
     */
    public void setValues(boolean[] values) {
        this.values = values;
        start();
    }

    @Override
    public void visit(ConstantNode node) {
        stack.push(node.getValue());
    }

    /**
     * @throws IllegalStateException if a given variable does not exist in the expression
     */
    @Override
    public void visit(VariableNode node) {
        Integer pos = positions.get(node.getName());

        if (pos == null) {
            throw new IllegalStateException("A given variable does not exist in the expression!");
        }
        stack.push(values[pos]);
    }

    @Override
    public void visit(UnaryOperatorNode node) {
        Util.visitChild(this, node.getChild());
        stack.push(node.getOperator().apply(stack.pop()));
    }

    @Override
    public void visit(BinaryOperatorNode node) {
        for (Node child : node.getChildren()) {
            Util.visitChild(this, child);
        }

        for (int i = 0, size = node.getChildren().size(); i < size - 1; i++) {
            stack.push(node.getOperator().apply(stack.pop(), stack.pop()));
        }
    }

    /**
     * Resets current evaluation and prepares for the next
     * evaluation.
     */
    public void start() {
        stack.clear();
    }

    /**
     * Returns the result of the given boolean expression.
     * The method can be called more than once, and will
     * always return the last evaluated result of the
     * expression.
     *
     * @return the result of the given boolean expression.
     * @throws IllegalStateException if the expression cannot
     *                               be evaluated
     */
    public boolean getResult() throws IllegalStateException {
        if (stack.size() != 1) {
            throw new IllegalStateException();
        }
        return stack.peek();
    }
}
