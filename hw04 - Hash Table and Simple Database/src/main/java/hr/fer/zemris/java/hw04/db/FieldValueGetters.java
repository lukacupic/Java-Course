package hr.fer.zemris.java.hw04.db;

/**
 * Represents the value getters for the fields of the {@link StudentRecord} object.
 *
 * @author Luka Čupić
 */
public class FieldValueGetters {

    /**
     * Represents a getter for the first name of a student record.
     */
    public static final IFieldValueGetter FIRST_NAME = studentRecord -> studentRecord.getFirstName();

    /**
     * Represents a getter for the last name of a student record.
     */
    public static final IFieldValueGetter LAST_NAME = (studentRecord) -> studentRecord.getLastName();

    /**
     * Represents a getter for the ID of a student record.
     */
    public static final IFieldValueGetter JMBAG = (studentRecord) -> studentRecord.getJmbag();
}
