package hr.fer.zemris.java.custom.collections;

/**
 * Represents a linked list collection of objects. Duplicate elements are
 * allowed; null-value elements are not allowed.
 *
 * @author Luka Čupić
 */
public class LinkedListIndexedCollection extends Collection {

    /**
     * Represents a node of the list.
     */
    private static class ListNode {
        ListNode previous;
        ListNode next;
        Object value;
    }

    /**
     * Size of the list, i.e. the number of nodes.
     */
    private int size;

    /**
     * Reference to the first element of the list.
     */
    private ListNode first;

    /**
     * Reference to the last element of the list.
     */
    private ListNode last;

    /**
     * The default constructor; creates an empty list.
     */
    public LinkedListIndexedCollection() {
        this.first = null;
        this.last = null;
    }

    /**
     * Creates a new list with all the elements from the passed collection.
     *
     * @param other the collection to add the elements from.
     */
    public LinkedListIndexedCollection(Collection other) {
        addAll(other);
        this.size = other.size();
    }

    @Override
    public int size() {
        return this.size;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if value is null.
     */
    public void add(Object value) throws IllegalArgumentException {
        // adding the element is equivalent to inserting it at the end
        insert(value, this.size);
    }

    /**
     * Returns the object that from the list at the position index.
     *
     * @param index the position of the element to be returned. Must be a
     *              non-negative number smaller than size of the list.
     * @return the object at the position index.
     * @throws IndexOutOfBoundsException if the index is invalid (i.e. a negative or a too large
     *                                   number)
     */
    public Object get(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException();
        }

        // get the node from position index
        ListNode current = iterate(index);

        return current.value;
    }

    @Override
    public void clear() {
        ListNode current = first;

        // clears all the previous and next references...
        for (int i = 0; i < this.size - 1; i++) {
            current.previous = null;
            current = current.next;
            current.previous.next = null;
        } // ... except for this one:
        current.previous = null;

        this.size = 0;
    }

    /**
     * Inserts the given value to the list at the given position.
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

        ListNode newNode = new ListNode();
        newNode.value = value;

        // if the list is empty
        if (this.size == 0) {
            this.first = newNode;
            this.last = newNode;
            this.size++;
            return;
        }

        // if element should be inserted at the beginning
        if (position == 0) {
            newNode.next = first;

            first.previous = newNode;

            first = newNode;

            // if element should be inserted at the end
        } else if (position == this.size) {
            newNode.previous = last;
            last.next = newNode;
            last = newNode;

            // if element should be inserted elsewhere
        } else {
            // get the node from position index
            ListNode current = iterate(position);

            newNode.next = current;
            newNode.previous = current.previous;
            current.previous.next = newNode;
            current.previous = newNode;
        }
        this.size++;
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

        ListNode current = first;

        for (int i = 0; i < this.size; i++) {
            if (current.value.equals(value)) {
                index = i;
                break;
            }
            current = current.next;
        }

        return index;
    }

    /**
     * Removes the element at specified index from the list.
     *
     * @param index the index of the element to be removed.
     * @throws IllegalArgumentException if the index is invalid (i.e. a negative or a too large
     *                                  number)
     */
    public void remove(int index) {
        if (index < 0 || index >= this.size) {
            throw new IllegalArgumentException();
        }

        // if the list has only one element
        if (this.size == 1) {
            this.first = null;
            this.last = null;
            this.size--;
            return;
        }

        // if the element should be removed from the beginning
        if (index == 0) {
            first.next.previous = null;
            first = first.next;

            // if the element should be removed from the end
        } else if (index == this.size - 1) {
            last.previous.next = null;
            last = last.previous;

            // if the element should be removed from elsewhere
        } else {
            // get the node from position index
            ListNode current = iterate(index);

            current.previous.next = current.next;
            current.next.previous = current.previous;

        }
        this.size--;
    }

    @Override
    public boolean remove(Object value) {
        int index = indexOf(value);

        // the object doesn't exist in the list
        if (index == -1) {
            return false;
        }

        remove(index);
        return true;
    }

    @Override
    public boolean contains(Object value) {
        // if the indexOf method returns -1, then the element doesn't exist
        return indexOf(value) != -1;
    }

    @Override
    public void forEach(Processor processor) {
        ListNode current = first;

        for (int i = 0; i < this.size; i++) {
            processor.process(current.value);
            current = current.next;
        }
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[this.size];
        ListNode current = first;

        for (int i = 0; i < this.size; i++) {
            array[i] = current.value;
            current = current.next;
        }

        return array;
    }

    /**
     * Iterates the list and returns the node at the position index.
     *
     * @param index the position of the node to be returned.
     * @return the node at the position index.
     */
    private ListNode iterate(int index) {
        ListNode current;

        // if the element is closer to the head, iterate from the left
        if (index <= this.size / 2) {
            current = this.first;

            for (int i = 0; i < index; i++) {
                current = current.next;
            }

            // it's closer to the tail so iterate from the right
        } else {
            current = this.last;

            for (int i = size - 1; i > index; i--) {
                current = current.previous;
            }
        }

        return current;
    }
}