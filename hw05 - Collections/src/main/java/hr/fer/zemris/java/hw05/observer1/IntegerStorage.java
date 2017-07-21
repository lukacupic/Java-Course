package hr.fer.zemris.java.hw05.observer1;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an observer-design-pattern Subject which stores a single {@link Integer} value.
 * The methods in this class allow the {@link IntegerStorageObserver} objects (called observers)
 * to register to this Subject so that they can be notified when the state (i.e. the value) is changed.
 *
 * @author Luka Čupić
 */
public class IntegerStorage {

	/**
	 * The stored value of this object.
	 */
	private int value;

	/**
	 * The list of observers.
	 */
	private List<IntegerStorageObserver> observers;

	/**
	 * A helper list for iterating through the observers; this list
	 * holds a copy of the {@link IntegerStorage#observers}.
	 */
	private List<IntegerStorageObserver> newObservers;

	/**
	 * Creates a new instance of this class with an initial value.
	 *
	 * @param initialValue the initial value of this object.
	 */
	public IntegerStorage(int initialValue) {
		this.value = initialValue;
		observers = new ArrayList<>();
		newObservers = new ArrayList<>();
	}

	/**
	 * Registers a new observer which will be automatically notified of
	 * any state changes of this Subject.
	 *
	 * @param observer the observer to register.
	 */
	public void addObserver(IntegerStorageObserver observer) {
		if (observers.contains(observer)) return;

		observers.add(observer);
	}

	/**
	 * Removes a registered observer from the Subject, thus making the
	 * previous observer unaware of any further changes of the state of this Subject.
	 *
	 * @param observer the observer to un-register.
	 */
	public void removeObserver(IntegerStorageObserver observer) {
		if (!observers.contains(observer)) return;

		observers.remove(observer);
	}

	/**
	 * Removes all registered observers from this Subjects, thus making all
	 * previous observers unaware of any further changes of the state of this Subject.
	 */
	public void clearObservers() {
		observers.clear();
	}

	/**
	 * Gets the stored value of this object.
	 *
	 * @return the stored value of this object.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Sets a new value of this object.
	 *
	 * @param value the new value of this object.
	 */
	public void setValue(int value) {
		if (this.value == value) return;

		this.value = value;

		for (IntegerStorageObserver observer : new ArrayList<>(observers)) {
			observer.valueChanged(this);
		}
	}
}
