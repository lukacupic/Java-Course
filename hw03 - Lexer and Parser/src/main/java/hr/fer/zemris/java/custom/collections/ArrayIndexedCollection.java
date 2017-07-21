package hr.fer.zemris.java.custom.collections;

/**
 * Represents a resizeable array collection of objects. Duplicate elements
 * are allowed; null-value elements are not allowed.
 *
 * @author Luka Čupić
 */
public class ArrayIndexedCollection extends Collection {

    /**
     * Current size of the collection.
     */
    private int size;

    /**
     * Current capacity of the allocated array of objects.
     */
    private int capacity;

    /**
     * Array of object references; it's length is determined by the capacity.
     * variable.
     */
    private Object[] elements;

    /**
     * The default value of the array.
     */
    private static final int DEFAULT_SIZE = 16;

    /**
     * The default constructor. Sets up the array with the default size.
     */
    public ArrayIndexedCollection() {
        this(DEFAULT_SIZE);
    }

    /**
     * Allocates the array with the given capacity.
     *
     * @param initialCapacity the initial capacity of the array. Must be a non-negative number.
     * @throws IllegalArgumentException if initialCapacity is a negative number.
     */
    public ArrayIndexedCollection(int initialCapacity) throws IllegalArgumentException {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException();
        }

        this.elements = new Object[initialCapacity];
        this.capacity = initialCapacity;
    }

    /**
     * Creates a new array with all the elements from the passed collection.
     *
     * @param collection the collection to add the elements from.
     */
    public ArrayIndexedCollection(Collection collection) {
        this(collection, collection.size());
    }

    /**
     * Allocates a new array with the capacity of initialCapacity. All the
     * elements from the passed collection are then added to this collection.
     *
     * @param collection      the collection to add the elements from.
     * @param initialCapacity initial capacity of the array.
     * @throws IllegalArgumentException if the collection size is larger than the initialCapacity.
     */
    public ArrayIndexedCollection(Collection collection, int initialCapacity) throws IllegalArgumentException {
        // the collection capacity is too small!
        if (collection.size() > initialCapacity) {
            throw new IllegalArgumentException();
        }

        this.capacity = initialCapacity;
        addAll(collection);
        this.size = collection.size();
    }

    @Override
    public int size() {
        return this.size;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if the value is null.
     */
    @Override
    public void add(Object value) throws IllegalArgumentException {
        // adding the element is equivalent to inserting it at the end
        insert(value, this.size);
    }

    @Override
    public boolean contains(Object value) {
        // if the indexOf method returns -1, then the element doesn't exist
        return indexOf(value) != -1;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public Object[] toArray() {
        Object[] temp = new Object[this.size];

        for (int i = 0; i < this.size; i++) {
            temp[i] = this.elements[i];
        }

        return temp;
    }

    @Override
    public boolean remove(Object value) {
        int index = indexOf(value);

        // the object doesn't exist in the array
        if (index == -1) {
            return false;
        }

        remove(index);
        return true;
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.size; i++) {
            this.elements[i] = null;
        }
        this.size = 0;
    }

    @Override
    public void forEach(Processor processor) {
        for (int i = 0; i < this.size; i++) {
            processor.process(this.elements[i]);
        }
    }

    /**
     * Removes element at specified index from the array.
     *
     * @param index the index of the element to be removed.
     * @throws IllegalArgumentException if the index is invalid (i.e. a negative or a too
     *                                  large number)
     */
    public void remove(int index) {
        if (index < 0 || index >= this.size) {
            throw new IllegalArgumentException();
        }

        for (int i = index; i < this.size; i++) {
            this.elements[i] = this.elements[i + 1];
        }
        this.size--;
    }

    /**
     * Searches the collection and returns index of the passed value.
     *
     * @param value the value to be found.
     * @return the index of the first occurrence of the given value or -1 if the
     * value is not found.
     */
    public int indexOf(Object value) {
        int index = -1;

        for (int i = 0, size = this.size; i < size; i++) {
            if (this.elements[i].equals(value)) {
                index = i;
            }
        }
        return index;
    }

    /**
     * Returns the element at the position index.
     *
     * @param index the position of the element to be returned. Must be a non-negative
     *              number smaller than size of the array.
     * @return the element at the position index.
     * @throws IndexOutOfBoundsException if the index is invalid (i.e. a negative or a too
     *                                   large number)
     */
    public Object get(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException();
        }

        return this.elements[index];
    }

    /**
     * Inserts the given value to the array at the given position.
     *
     * @param value    the value to be inserted.
     * @param position must be a non-negative number smaller than or equal to the
     *                 size of the array.
     * @throws IllegalArgumentException if position is invalid or null value is passed as value.
     */
    public void insert(Object value, int position) throws IllegalArgumentException {
        if (position < 0 || position > this.size || value == null) {
            throw new IllegalArgumentException();
        }

        // if the array is full, double it's size before inserting a new element
        if (this.size == this.capacity) {
            this.capacity *= 2;
            Object[] temp = new Object[this.capacity];

            // copy all the elements from the current array to the new one
            for (int i = 0; i < this.size; i++) {
                temp[i] = elements[i];
            }
            this.elements = temp;
        }

        // shift elements one place to the right
        for (int i = this.size - 1; i >= position; i--) {
            this.elements[i + 1] = this.elements[i];
        }
        this.elements[position] = value;
        this.size++;
    }
}