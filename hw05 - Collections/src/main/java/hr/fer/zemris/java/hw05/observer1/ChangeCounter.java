package hr.fer.zemris.java.hw05.observer1;

/**
 * Represents an instance of the {@link IntegerStorageObserver} which, when registered
 * to a new Subject ({@link IntegerStorage}, begins counting the number of times the
 * state of the Subject has changed.
 * Upon being notified by the Subject of a new change, this object will automatically
 * write to the standard output the number of changes made since the registration to the Subject.
 *
 * @author Luka Čupić
 */
public class ChangeCounter implements IntegerStorageObserver {

	/**
	 * Represents the internal counter of changes.
	 */
	private int counter;

	/**
	 * Creates a new instance of this object.
	 */
	public ChangeCounter() {
		counter = 0;
	}

	@Override
	public void valueChanged(IntegerStorage istorage) {
		System.out.println("Number of value changes since tracking: " + ++counter);
	}
}
