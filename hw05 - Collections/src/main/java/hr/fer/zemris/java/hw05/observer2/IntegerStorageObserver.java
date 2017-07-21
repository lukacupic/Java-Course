package hr.fer.zemris.java.hw05.observer2;

/**
 * Represents an observer-design-pattern Observer which is automatically notified
 * through the {@link IntegerStorageObserver#valueChanged} method when the state
 * of the Subject ({@link IntegerStorage}) this Observer is registered to has changed.
 *
 * @author Luka Čupić
 */
public interface IntegerStorageObserver {

    /**
     * This method will be called automatically for any changes made to the
     * state of the Subject this Observer is registered to.
     *
     * @param istorage a reference to the Subject whose state has been changed.
     */
    void valueChanged(IntegerStorageChange istorage);
}
