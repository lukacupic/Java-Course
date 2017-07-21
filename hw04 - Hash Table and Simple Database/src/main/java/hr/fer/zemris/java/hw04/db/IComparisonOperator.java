package hr.fer.zemris.java.hw04.db;

/**
 * Represents a comparison operator for a logical condition of the query.
 * <p>
 * All classes implementing this interface must provide an implementation
 * of the {@link IComparisonOperator#satisfied(String, String)} method so
 * that is uses some kind of comparison between the two passed strings.
 *
 * @author Luka Čupić
 */
public interface IComparisonOperator {

    /**
     * Checks whether the two passed string literals satisfy this comparison
     * operator.
     *
     * @param value1 the first string literal.
     * @param value2 the second string literal.
     * @return true if and only if the passed arguments satisfy the comparison
     * operator.
     */
    boolean satisfied(String value1, String value2);
}