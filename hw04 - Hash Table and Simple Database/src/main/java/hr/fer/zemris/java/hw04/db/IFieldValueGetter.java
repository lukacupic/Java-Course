package hr.fer.zemris.java.hw04.db;

/**
 * Obtains a requested field value from the given {@link StudentRecord} object.
 * <p>
 * All classes implementing this interface must provide an implementation of the
 * {@link IFieldValueGetter#get(StudentRecord)} method which returns a string
 * representation of the field value of the passed {@link StudentRecord} object.
 *
 * @author Luka Čupić
 */
public interface IFieldValueGetter {

    /**
     * Returns a string representation of the chosen {@link StudentRecord} field.
     *
     * @param record the student record to get the field from.
     * @return a string representation of the chosen {@link StudentRecord} field.
     */
    String get(StudentRecord record);
}
