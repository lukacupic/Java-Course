package hr.fer.zemris.java.hw05.observer2;

/**
 * An instance of this class encapsulates an {@link IntegerStorage} object,
 * alongside it's previous and current state values.
 *
 * @author Luka Čupić
 */
public class IntegerStorageChange {

    /**
     * The encapsulated integer storage object.
     */
    private IntegerStorage integerStorage;

    /**
     * The previous value of the integer storage object.
     */
    private int oldValue;

    /**
     * The current value of the integer storage object.
     */
    private int newValue;

    /**
     * Creates a new instance of this class.
     *
     * @param integerStorage the integer storage object to encapsulate.
     * @param oldValue       the previous state of the integer storage.
     * @param newValue       the current state of the integer storage.
     */
    public IntegerStorageChange(IntegerStorage integerStorage, int oldValue, int newValue) {
        this.integerStorage = integerStorage;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    /**
     * Gets the integer storage object.
     *
     * @return the encapsulated integer storage object.
     */
    public IntegerStorage getIntegerStorage() {
        return integerStorage;
    }

    /**
     * Gets the previous value which the storage object had.
     *
     * @return the previous value of the storage object.
     */
    public int getOldValue() {
        return oldValue;
    }

    /**
     * Gets the current value of the storage object.
     *
     * @return the current value of the storage object.
     */
    public int getNewValue() {
        return newValue;
    }
}
