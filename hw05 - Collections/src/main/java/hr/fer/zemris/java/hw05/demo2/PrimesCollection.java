package hr.fer.zemris.java.hw05.demo2;

import java.util.NoSuchElementException;

/**
 * Represents a collection of prime numbers. This collection provides
 * an iterator so that the numbers can be iterated in a for-each loop.
 *
 * @author Luka Čupić
 */
public class PrimesCollection implements Iterable<Integer> {

	/**
	 * The number of prime numbers this collection is capable of providing.
	 */
	private final int number;

	/**
	 * Creates a new prime collection.
	 *
	 * @param number the number of primes this collection will be capable
	 *               of providing.
	 */
	public PrimesCollection(int number) {
		this.number = number;
	}

	/**
	 * Checks if the given number is prime.
	 *
	 * @param number the number to check.
	 * @return true if the number is prime; false otherwise.
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
	 * Returns the index-th prime number. Legal indexes are strictly
	 * greater than 1 and all others will cause an {@link IllegalStateException};
	 *
	 * @param index the ordinal number of a prime number to get.
	 * @return true if the number is prime; false otherwise.
	 * @throws IllegalStateException if the given index is smaller than 1.
	 */
	private int getPrime(int index) throws IllegalStateException {
		if (index <= 0) {
			throw new IllegalArgumentException();
		}
		int currentIndex = 0;

		for (int i = 2; ; i++) {
			if (isPrime(i)) {
				currentIndex++;
			}

			if (currentIndex == index) {
				return i;
			}
		}
	}

	@Override
	public Iterator iterator() {
		return new Iterator();
	}

	/**
	 * The iterator for the {@link PrimesCollection} class.
	 */
	private class Iterator implements java.util.Iterator<Integer> {

		/**
		 * Internal counter variable; used for iterating the primes.
		 */
		private int count = 0;

		@Override
		public boolean hasNext() {
			return count < number;
		}

		@Override
		public Integer next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}

			return getPrime(++count);
		}
	}
}
