package hr.fer.zemris.bf.utils;

import hr.fer.zemris.bf.model.*;
import hr.fer.zemris.bf.parser.Parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a node visitor which prints all variables contained
 * in the expression given to the {@link Parser} object.
 *
 * @author Luka Čupić
 */
public class VariablesGetter implements NodeVisitor {

    /**
     * Represents all variables in the given expression.
     */
    private List<VariableNode> variables;

    /**
     * Creates a new instance of this class.
     */
    public VariablesGetter() {
        variables = new ArrayList<>();
    }

    @Override
    public void visit(ConstantNode node) {
        // do nothing - sorry
    }

    @Override
    public void visit(VariableNode node) {
        variables.add(node);
    }

    @Override
    public void visit(UnaryOperatorNode node) {
        Util.visitChild(this, node.getChild());
    }

    @Override
    public void visit(BinaryOperatorNode node) {
        for (Node child : node.getChildren()) {
            Util.visitChild(this, child);
        }
    }

    /**
     * Returns a list of all variables contained in the given
     * expression. The list represents variables by their names,
     * and therefore holds objects of type {@link String}.
     *
     * @return the list of variables
     */
    public List<String> getVariables() {
        List<String> varNames = new ArrayList<>();
        for (VariableNode var : variables) {
            varNames.add(var.getName());
        }
        varNames.sort(String::compareTo);
        return varNames;
    }
}
