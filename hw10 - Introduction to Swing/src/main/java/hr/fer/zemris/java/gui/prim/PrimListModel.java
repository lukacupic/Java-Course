package hr.fer.zemris.java.gui.prim;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an implementation of a simple list model
 * which generates contiguous prime numbers. Each new prime number
 * can be obtained by calling the method {@link #next()}.
 *
 * @author Luka Čupić
 */
public class PrimListModel implements ListModel<Integer> {

    /**
     * Represents the list of all generated prime numbers.
     */
    private List<Integer> primes;

    /**
     * Represents last generated prime number.
     */
    private int lastPrime;

    /**
     * Represents all listeners of this model.
     */
    private List<ListDataListener> listeners;

    /**
     * Creates a new instance of this class.
     */
    public PrimListModel() {
        primes = new ArrayList<>();
        listeners = new ArrayList<>();
    }

    @Override
    public int getSize() {
        return primes.size();
    }

    @Override
    public Integer getElementAt(int index) {
        return primes.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }

    /**
     * Generates the next prime number.
     */
    public void next() {
        lastPrime = nextPrime();
        primes.add(lastPrime);

        ListDataEvent e = new ListDataEvent(
            this, ListDataEvent.INTERVAL_ADDED, lastPrime, lastPrime
        );
        for (ListDataListener l : listeners) {
            l.intervalAdded(e);
        }
    }

    /**
     * Checks if the specified number is prime.
     *
     * @param number the number to check
     * @return true if the given number is prime; false otherwise
     */
    private boolean isPrime(int number) {
        if (number <= 1) return false;
        if (number == 2) return true;
        if (number % 2 == 0) return false;

        for (int i = 3; i * i <= number; i += 2) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the next prime number.
     *
     * @return the first prime number greater than {@link #lastPrime}
     */
    private int nextPrime() {
        for (int num = lastPrime + 1; ; num++) {
            if (isPrime(num)) {
                return num;
            }
        }
    }
}
