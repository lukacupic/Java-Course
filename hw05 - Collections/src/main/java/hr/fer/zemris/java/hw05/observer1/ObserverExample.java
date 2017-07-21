package hr.fer.zemris.java.hw05.observer1;

/**
 * This is an example program for the {@link IntegerStorage} Subject
 * and the {@link IntegerStorageObserver} Observers.
 *
 * @author Luka Čupić
 */
public class ObserverExample {

	/**
	 * The main method of this class.
	 *
	 * @param args command line arguments; not used in this program.
	 */
	public static void main(String[] args) {
		IntegerStorage istorage = new IntegerStorage(20);

		IntegerStorageObserver observer = new SquareValue();

		istorage.addObserver(observer);
		istorage.setValue(5);
		istorage.setValue(2);
		istorage.setValue(25);

		istorage.removeObserver(observer);

		istorage.addObserver(new ChangeCounter());
		istorage.addObserver(new DoubleValue(1));
		istorage.addObserver(new DoubleValue(2));
		istorage.addObserver(new DoubleValue(2));

		istorage.setValue(13);
		istorage.setValue(22);
		istorage.setValue(15);
	}
}