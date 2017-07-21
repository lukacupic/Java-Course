package hr.fer.zemris.java.hw04.db;

/**
 * Represents a filter for the {@link StudentRecord} objects.
 * <p>
 * All classes implementing this interface must provide an implementation
 * which either accepts an object or does not, based on an arbitrary condition.
 *
 * @author Luka Čupić
 */
public interface IFilter {

    /**
     * Evaluates a condition upon a given {@link StudentRecord} object and returns
     * true if and only if the object satisfies a placed condition.
     *
     * @param record the object to evaluate.
     * @return true if and only if the object satisfies a condition; false otherwise.
     */
    boolean accepts(StudentRecord record);
}