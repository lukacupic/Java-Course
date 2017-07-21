package hr.fer.zemris.java.custom.collections;

/**
 * Represents a stack collection of objects. Duplicate elements
 * are allowed; null-value elements are not allowed.
 *
 * @author Luka Čupić
 */
public class ObjectStack {

    /**
     * Array which will be used as an internal collection of objects.
     */
    private ArrayIndexedCollection array;

    /**
     * The default constructor which sets up the internal array.
     */
    public ObjectStack() {
        array = new ArrayIndexedCollection();
    }

    /**
     * Checks if the stack is empty.
     *
     * @return true if the stack is empty, false otherwise.
     */
    public boolean isEmpty() {
        return array.isEmpty();
    }

    /**
     * Returns the size of the stack.
     *
     * @return the number of objects currently stored in this collections.
     */
    public int size() {
        return array.size();
    }

    /**
     * Removes all elements from the stack.
     */
    public void clear() {
        array.clear();
    }

    /**
     * Inserts a new element at the top of the stack.
     *
     * @param value the element to be pushed on the stack; null is not allowed.
     * @throws IllegalArgumentException if null is passed as value.
     */
    public void push(Object value) throws IllegalArgumentException {
        if (value == null) {
            throw new IllegalArgumentException();
        }

        array.add(value);
    }

    /**
     * Removes the last value pushed to the stack and returns it.
     *
     * @return the element from the top of the stack.
     * @throws EmptyStackException if the method is called for an empty stack.
     */
    public Object pop() throws EmptyStackException {
        int size = array.size();

        if (size == 0) {
            throw new EmptyStackException();
        }

        // take the last element and then remove it
        Object temp = array.get(size - 1);
        array.remove(size - 1);

        return temp;
    }

    /**
     * Returns the last element placed on the stack but does not delete it.
     *
     * @return the element from the top of the stack.
     * @throws EmptyStackException if the method is called for an empty stack.
     */
    public Object peek() throws EmptyStackException {
        Object temp = pop();
        push(temp);
        return temp;
    }
}