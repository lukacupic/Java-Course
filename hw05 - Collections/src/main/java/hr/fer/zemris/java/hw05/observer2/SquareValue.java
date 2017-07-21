package hr.fer.zemris.java.hw05.observer2;

/**
 * Represents an instance of the {@link IntegerStorageObserver} which, when notified
 * by the Subject ({@link IntegerStorage} of the changed state, writes to the standard
 * output the square of the new value stored in the Subject.
 *
 * @author Luka Čupić
 */
public class SquareValue implements IntegerStorageObserver {

    /**
     * Creates a new instance of this object.
     */
    public SquareValue() {
        super();
    }

    @Override
    public void valueChanged(IntegerStorageChange istorageChange) {
        int value = istorageChange.getNewValue();

        System.out.printf("Provided new value: %d, square is %d%n", value, value * value);
    }
}
