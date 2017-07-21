package hr.fer.zemris.java.hw04.collections;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Represents a simple hash table which permits null values but does not permit
 * null keys.
 * <p>
 * This implementation of hash table provides constant-time performance of operations
 * such as get and put.
 * <p>
 * The table has a default value of 0.75 for the load factor, meaning that each time the
 * table's size reaches 75% of it's capacity, the capacity will be reallocated to be twice
 * the previous.
 *
 * @param <K> the key type of the table entry.
 * @param <V> the value type of the table entry.
 * @author Luka Čupić
 */
public class SimpleHashTable<K, V> implements Iterable<SimpleHashTable.TableEntry<K, V>> {

	/**
	 * Array for storing the has table entries.
	 */
	private TableEntry<K, V>[] table;

	/**
	 * The size of the hash table.
	 */
	private int size;

	/**
	 * Counter for any modification of this collection which results in a different
	 * number of entries (e.g. removing or adding a new element).
	 */
	private int modificationCount;

	/**
	 * The default capacity of the hash table.
	 */
	private static final int DEFAULT_CAPACITY = 16;

	/**
	 * Holds the percentage for the optimal size of the hash table.
	 * After this value is reached, the table should be reallocated.
	 */
	private static final double LOAD_FACTOR = 0.75;

	/**
	 * The default constructor.
	 * Creates a hash table with capacity of {@link SimpleHashTable#DEFAULT_CAPACITY}.
	 */
	public SimpleHashTable() {
		this(DEFAULT_CAPACITY);
	}

	/**
	 * Creates a hash table whose capacity will be the first power of 2
	 * larger than or equal to the given argument
	 *
	 * @param capacity the capacity of the hash table. Is modified as explained
	 *                 in this method's javadoc.
	 */
	@SuppressWarnings("unchecked")
	public SimpleHashTable(int capacity) {
		if (capacity < 1) {
			throw new IllegalArgumentException();
		}

		int realCapacity = (int) Math.pow(2, Math.ceil(Math.log(capacity) / Math.log(2)));

		table = (TableEntry<K, V>[]) new TableEntry[realCapacity];
	}

	/**
	 * Inserts a new key-value pair into hash table.
	 *
	 * @param key   the key of the pair. Must not be null.
	 * @param value the value of the pair.
	 */
	public void put(K key, V value) {
		if (key == null) {
			throw new IllegalArgumentException();
		}

		// check capacity; reallocate the table if needed
		if (size >= LOAD_FACTOR * table.length) {
			reallocate();
		}

		TableEntry<K, V> entry = new TableEntry<>(key, value);

		int slot = getTableIndex(key);

		if (table[slot] == null) {
			table[slot] = entry;
			size++;
			modificationCount++;
			return;
		}

		TableEntry<K, V> current = table[slot];

		while (current.next != null) {
			// update the value if the entry already exists
			if (key.equals(current.key)) {
				current.setValue(value);
				return;
			}
			current = current.next;
		}
		current.next = entry;
		size++;
		modificationCount++;
	}

	/**
	 * Returns the value of an entry specified by the passed key.
	 * If an entry with the passed key does not exist, the method returns null.
	 *
	 * @param key the key of the entry.
	 * @return the value of the entry.
	 */
	public V get(Object key) {
		if (key == null) {
			return null;
		}

		K legalKey;
		try {
			legalKey = (K) key;
		} catch (ClassCastException ex) {
			return null;
		}

		int slot = getTableIndex(legalKey);

		if (table[slot] != null) {
			TableEntry<K, V> current = table[slot];
			do {
				if (current.getKey().equals(key))
					return current.getValue();
			} while ((current = current.next) != null);
		}
		return null;
	}

	/**
	 * Returns the number of entries in this hash table.
	 *
	 * @return the size of this hash table.
	 */

	public int size() {
		return size;
	}

	/**
	 * Checks if the hash table contains a key.
	 *
	 * @param key the key to check.
	 * @return true if and only if the table contains a key.
	 */
	public boolean containsKey(Object key) {
		if (key == null) {
			return false;
		}
		return get(key) != null;
	}

	/**
	 * Checks if the hash table contains a value.
	 *
	 * @param value the value to check.
	 * @return true if and only if the table contains a certain value.
	 */
	public boolean containsValue(Object value) {
		V legalValue;
		try {
			legalValue = (V) value;
		} catch (ClassCastException ex) {
			return false;
		}

		for (int i = 0; i < table.length; i++) {
			if (table[i] == null) continue;

			TableEntry<K, V> current = table[i];
			do {
				if (current.getValue().equals(legalValue)) {
					return true;
				}
			} while ((current = current.next) != null);
		}
		return false;
	}


	/**
	 * Removes the entry with the specified key from the table.
	 *
	 * @param key the key of the entry to remove.
	 */
	public void remove(Object key) {
		if (key == null) {
			return;
		}

		K legalKey;
		try {
			legalKey = (K) key;
		} catch (ClassCastException ex) {
			return;
		}

		int slot = getTableIndex(legalKey);
		if (table[slot] != null) {
			TableEntry<K, V> current = table[slot];
			TableEntry<K, V> previous = null;

			while (current != null) {
				if (current.getKey().equals(key)) {
					if (current == table[slot]) {
						table[slot] = current.next;
					} else {
						previous.next = current.next;
					}
					this.size--;
					modificationCount++;
					return;
				}
				previous = current;
				current = current.next;
			}
		}

	}

	/**
	 * Checks if the hash table is empty.
	 *
	 * @return true if the hash has no entries; false otherwise.
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * @return
	 */
	public String toString() {
		StringBuilder entries = new StringBuilder();
		entries.append("[");

		for (int i = 0; i < table.length; i++) {
			if (table[i] == null) continue;

			TableEntry<K, V> current = table[i];
			do {
				entries.append(current.getKey());
				entries.append(" => ");
				entries.append(current.getValue());
				entries.append(", ");
			} while ((current = current.next) != null);
		}
		// remove the last coma and space
		int length = entries.length();
		entries.delete(length - 2, length);

		entries.append("]");
		return entries.toString();
	}

	/**
	 * Reallocates the hash table to be twice the current capacity.
	 */
	@SuppressWarnings("unchecked")
	private void reallocate() {
		TableEntry<K, V>[] oldTable = table; // store the (old) table temporarily
		table = (TableEntry<K, V>[]) new TableEntry[table.length * 2]; // new table
		size = 0;
		modificationCount++;

		// iterate through the old table and put all entries into the new table
		for (int i = 0; i < oldTable.length; i++) {
			if (oldTable[i] == null) continue;

			TableEntry<K, V> current = oldTable[i];
			do {
				put(current.getKey(), current.getValue());
			} while ((current = current.next) != null);
		}
	}

	/**
	 * Irretrievably obliterates all entries from this hash table.
	 */
	public void clear() {
		// clearing head references is enough for the remaining nodes
		// to become eligible for garbage collection
		for (int i = 0; i < table.length; i++) {
			table[i] = null;
		}
		size = 0;
		modificationCount++;
	}

	@Override
	public Iterator<TableEntry<K, V>> iterator() {
		return new IteratorImpl();
	}

	/**
	 * Calculates and returns the index of the entry with key {@param key}.
	 *
	 * @param key the key of the entry.
	 * @return index of the entry with key {@param key}.
	 */
	private int getTableIndex(K key) {
		return Math.abs(key.hashCode()) % table.length;
	}

	/**
	 * A helper class for modeling a single entry of the table.
	 *
	 * @param <K> the key type of the table entry.
	 * @param <V> the value type of the table entry.
	 * @author Luka Čupić
	 */
	public static class TableEntry<K, V> {

		/**
		 * The key of the entry.
		 */
		private K key;

		/**
		 * The value of the entry.
		 */
		private V value;

		/**
		 * The reference to the next entry from the same table slot.
		 */
		private TableEntry<K, V> next;

		/**
		 * The constructor of the TableEntry object.
		 *
		 * @param key   the key of the entry.
		 * @param value the value of the entry.
		 */
		public TableEntry(K key, V value) {
			if (key == null) {
				throw new IllegalArgumentException();
			}

			this.key = key;
			this.value = value;
		}

		/**
		 * Returns the key of this entry.
		 *
		 * @return the key of this entry.
		 */
		public K getKey() {
			return key;
		}

		/**
		 * Returns the value of this entry.
		 *
		 * @return the value of this entry.
		 */
		public V getValue() {
			return value;
		}

		/**
		 * Sets the value of this entry.
		 *
		 * @param value the value of this entry.
		 */
		public void setValue(V value) {
			this.value = value;
		}
	}

	/**
	 * An iterator over a {@link SimpleHashTable} collection of entries.
	 */
	private class IteratorImpl implements Iterator<TableEntry<K, V>> {

		/**
		 * The index of the slot;
		 */
		private int index;

		/**
		 * Represents the last iterated element.
		 */
		private TableEntry<K, V> entry;

		/**
		 * A counter for the visited number of entries.
		 */
		private int entryCount;

		/**
		 * Stores the number of modifications made to the hash table
		 * so that it can be checked whether some modifications were
		 * made while iterating the table.
		 */
		private int iteratorModificationCount;

		/**
		 * The default constructor.
		 */
		public IteratorImpl() {
			super();
			iteratorModificationCount = modificationCount;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @throws ConcurrentModificationException if the hash table hash been modified
		 *                                         from the outside while this iterator was active.
		 */
		@Override
		public boolean hasNext() {
			if (iteratorModificationCount != modificationCount) {
				throw new ConcurrentModificationException();
			}

			return entryCount < size;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @throws ConcurrentModificationException if the hash table hash been modified
		 *                                         from the outside while this iterator was active.
		 */
		@Override
		public TableEntry<K, V> next() {
			if (iteratorModificationCount != modificationCount) {
				throw new ConcurrentModificationException();
			}

			// if both the last entry and the reference to the next exist,
			// then return the next entry
			if (entry != null && entry.next != null) {
				entryCount++;
				entry = entry.next;
				return entry;
			}

			int oldIndex = index;

			// get to the first non-null element
			while (index + 1 < table.length) {
				index++;
				if (table[index] != null) break;
			}

			// if no elements were found during this iteration
			if (table[index] == null || oldIndex == index) {
				throw new NoSuchElementException();
			}

			entryCount++;
			entry = table[index];
			return entry;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @throws ConcurrentModificationException if the hash table hash been modified
		 *                                         from the outside while this iterator was active.
		 */
		@Override
		public void remove() {
			if (iteratorModificationCount != modificationCount) {
				throw new ConcurrentModificationException();
			}

			SimpleHashTable.this.remove(entry.getKey());

			iteratorModificationCount++;
			entryCount--;

			// if this method has been called more than once for a single entry,
			// throw an exception
			if (iteratorModificationCount > modificationCount) {
				throw new IllegalStateException();
			}
		}
	}
}
