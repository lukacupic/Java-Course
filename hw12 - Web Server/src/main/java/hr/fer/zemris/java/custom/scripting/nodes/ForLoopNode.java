package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;

/**
 * A node of the document model which represents a for-loop construct.
 *
 * @author Luka Čupić
 */
public class ForLoopNode extends Node {

    /**
     * The variable element of the construct.
     */
    private ElementVariable variable;

    /**
     * The start expression element of the construct.
     */
    private Element startExpression;

    /**
     * The end expression element of the construct.
     */
    private Element endExpression;

    /**
     * The step expression element of the construct.
     */
    private Element stepExpression;

    /**
     * The constructor for initializing the ForLoopNode object. The step value
     * of the loop can be omitted.
     *
     * @param variable        the variable to iterate.
     * @param startExpression the starting value of the iteration.
     * @param endExpression   the ending value of the iteration.
     * @param stepExpression  the step value of the iteration. Can be null.
     */
    public ForLoopNode(ElementVariable variable, Element startExpression,
                       Element endExpression, Element stepExpression) {

        if (variable == null || startExpression == null || endExpression == null) {
            throw new IllegalArgumentException();
        }

        this.variable = variable;
        this.startExpression = startExpression;
        this.endExpression = endExpression;
        this.stepExpression = stepExpression;
    }

    @Override
    public void accept(INodeVisitor visitor) {
        visitor.visitForLoopNode(this);
    }

    /**
     * Returns the value of the variable.
     *
     * @return the value of the variable.
     */
    public ElementVariable getVariable() {
        return variable;
    }

    /**
     * Returns the starting value.
     *
     * @return the starting value.
     */
    public Element getStartExpression() {
        return startExpression;
    }

    /**
     * Returns the ending value.
     *
     * @return the ending value.
     */
    public Element getEndExpression() {
        return endExpression;
    }

    /**
     * Returns the step value.
     *
     * @return the step value.
     */
    public Element getStepExpression() {
        return stepExpression;
    }
}