package hr.fer.zemris.java.hw04.db;

/**
 * Represents a conditional expression of a database input query.
 *
 * @author Luka Čupić
 */
public class ConditionalExpression {

    /**
     * Represents a {@link StudentRecord} attribute which will be compared.
     */
    private IFieldValueGetter fieldGetter;

    /**
     * Represents a string which the {@link ConditionalExpression#fieldGetter} will be compared to.
     */
    private String stringLiteral;

    /**
     * Represents a comparison comparisonOperator.
     */
    private IComparisonOperator comparisonOperator;

    /**
     * Creates a new conditional expression with the given arguments.
     *
     * @param fieldGetter        the attribute whose value will be compared.
     * @param stringLiteral      the value which the fieldGetter argument will be compared to.
     * @param comparisonOperator the comparisonOperator to perform this conditional expression.
     */
    public ConditionalExpression(IFieldValueGetter fieldGetter, String stringLiteral, IComparisonOperator comparisonOperator) {
        this.fieldGetter = fieldGetter;
        this.stringLiteral = stringLiteral;
        this.comparisonOperator = comparisonOperator;
    }

    /**
     * Returns the field getter.
     *
     * @return the field getter.
     */
    public IFieldValueGetter getFieldGetter() {
        return fieldGetter;
    }

    /**
     * Returns the string literal.
     *
     * @return the string literal.
     */
    public String getStringLiteral() {
        return stringLiteral;
    }

    /**
     * Returns the comparison operator.
     *
     * @return the comparison operator.
     */
    public IComparisonOperator getComparisonOperator() {
        return comparisonOperator;
    }
}
