package hr.fer.zemris.java.custom.collections;

/**
 * Represents a general collection of objects.
 *
 * @author Luka Čupić
 */
public class Collection {

    /**
     * The default constructor.
     */
    protected Collection() {
        super();
    }

    /**
     * Checks whether the collection is empty.
     *
     * @return true if the collection contains no objects, false otherwise.
     */
    public boolean isEmpty() {
        if (size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * Returns size of the collection.
     *
     * @return the number of objects currently stored in this collections.
     */
    public int size() {
        return 0;
    }

    /**
     * Adds the given object into the collection.
     *
     * @param value the value to be added.
     */
    public void add(Object value) {
        // does nothing
    }

    /**
     * Checks whether the collection contains the given value.
     *
     * @param value the value that will be checked; null value is permitted.
     * @return true only if the collection contains the given value, false
     * otherwise.
     */
    public boolean contains(Object value) {
        return false;
    }

    /**
     * Removes a single value from the collection.
     *
     * @param value the value to be removed.
     * @return true only if the collection contains the given value and if that
     * value has been successfully removed, false otherwise.
     */
    public boolean remove(Object value) {
        return false;
    }

    /**
     * Returns an array representation of this collection.
     *
     * @return an array with all the elements from the collection. The return
     * value is never null.
     */
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    /**
     * Processes all elements in this collection by calling the process method
     * of the passed processor object.
     *
     * @param processor the processor which will process the elements of this
     *                  collection.
     */
    public void forEach(Processor processor) {
        // does nothing
    }

    /**
     * Adds all the elements from the passed collection into this collection.
     *
     * @param other the collection which elements are to be added to this
     *              collection.
     */
    public void addAll(Collection other) {
        class P extends Processor {
            @Override
            public void process(Object value) {
                add(value);
            }
        }

        other.forEach(new P());
    }

    /**
     * Removes all elements from this collection.
     */
    public void clear() {
        // does nothing
    }
}