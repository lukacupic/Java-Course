package hr.fer.zemris.java.hw05.observer2;

/**
 * Represents an instance of the {@link IntegerStorageObserver} which, when notified
 * by the Subject ({@link IntegerStorage} of the changed state, writes to the standard
 * output the double value (i.e. value*2) stored in the Subject.
 * The object will perform this procedure only for a certain number of times. The number
 * of times this procedure will occur is chosen while creating the object itself, by
 * passing the said value through the constructor.
 * After the procedure has been performed by a certain number of times, the object
 * automatically un-registers itself from the Subject.
 *
 * @author Luka Čupić
 */
public class DoubleValue implements IntegerStorageObserver {

    /**
     * Represents the number of times this object will write to the standard input.
     */
    private final int n;

    /**
     * Represents the number of times the Subject's value has been changed.
     */
    private int counter;

    /**
     * Creates a new instance of this object.
     *
     * @param n the number of times this object will write to standard input.
     */
    public DoubleValue(int n) {
        this.n = n;
    }

    @Override
    public void valueChanged(IntegerStorageChange istorageChange) {
        System.out.println("Double value: " + istorageChange.getNewValue() * 2);
        counter++;

        if (counter >= n) {
            istorageChange.getIntegerStorage().removeObserver(this);
        }
    }
}