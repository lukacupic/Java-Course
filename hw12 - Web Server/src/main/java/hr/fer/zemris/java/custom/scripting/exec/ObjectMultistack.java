package hr.fer.zemris.java.custom.scripting.exec;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a collection of {@link MultistackEntry} objects, where
 * each individual stack is denoted by a key of type {@link String}.
 * <p>
 * This implementation provides constant-time performance for the basic operations,
 * such as push and pop.
 *
 * @author Luka Čupić
 */
public class ObjectMultistack {

    /**
     * An internal map for storing stacks.
     */
    private Map<String, MultistackEntry> map;

    /**
     * Creates a new instance of this class.
     */
    public ObjectMultistack() {
        map = new HashMap<>();
    }

    /**
     * Pushes a new {@link ValueWrapper} object to the top of the stack denoted by
     * the key {@param name}.
     *
     * @param name         the key of the stack to push to.
     * @param valueWrapper the value to push to the stack.
     */
    public void push(String name, ValueWrapper valueWrapper) {
        MultistackEntry newEntry = new MultistackEntry(valueWrapper, map.get(name));
        map.put(name, newEntry);
    }

    /**
     * Removes the {@link ValueWrapper} object from the top of the stack denoted by
     * the key {@param name} and returns a reference to it.
     *
     * @param name the key of the stack to pop from.
     * @throws EmptyStackException           if the stack denoted by {@param name} is empty.
     * @throws UnsupportedOperationException if the stack denoted by {@param name} doesn't exist.
     * @returns the {@link ValueWrapper} value from the top of the stack.
     */
    public ValueWrapper pop(String name) {
        if (!map.containsKey(name)) {
            throw new UnsupportedOperationException("Unknown stack key!");
        }

        MultistackEntry stackEntry = map.get(name);
        if (stackEntry == null) {
            throw new EmptyStackException();
        }
        map.put(name, map.get(name).next);

        return stackEntry.valueWrapper;
    }

    /**
     * Returns the value from the top of the stack denoted by the key {@param name}
     * but does not remove it, unlike the {@link ObjectMultistack#pop(String)} method.
     *
     * @param name the key of the stack to peek at.
     * @return the {@link ValueWrapper} value from the top of the stack.
     * @throws EmptyStackException           if the stack denoted by {@param name} is empty.
     * @throws UnsupportedOperationException if the stack denoted by {@param name} doesn't exist.
     */
    public ValueWrapper peek(String name) {
        if (!map.containsKey(name)) {
            throw new UnsupportedOperationException("Unknown stack key!");
        }

        ValueWrapper value = pop(name);
        push(name, value);

        return value;
    }

    /**
     * Checks if a stack denoted by the key {@param key} is empty.
     *
     * @param name the key of the stack.
     * @return true if and only if the selected stack contains no entries.
     * @throws UnsupportedOperationException if the stack denoted by {@param name} doesn't exist.
     */
    public boolean isEmpty(String name) {
        if (!map.containsKey(name)) {
            throw new UnsupportedOperationException("Unknown stack key!");
        }

        return map.get(name) == null;
    }

    /**
     * An entry which represents the {@link ValueWrapper} object, but also
     * contains a reference to another {@link MultistackEntry} object so that
     * the entries can be linked in a list.
     *
     * @author Luka Čupić
     */
    private static class MultistackEntry {

        /**
         * The value of the entry.
         */
        private ValueWrapper valueWrapper;

        /**
         * The reference to the next entry of the list.
         */
        private MultistackEntry next;

        /**
         * Creates a new instance of this class.
         *
         * @param valueWrapper the value of this entry.
         * @param next         the reference to next entry of the list.
         */
        public MultistackEntry(ValueWrapper valueWrapper, MultistackEntry next) {
            this.valueWrapper = valueWrapper;
            this.next = next;
        }
    }
}
