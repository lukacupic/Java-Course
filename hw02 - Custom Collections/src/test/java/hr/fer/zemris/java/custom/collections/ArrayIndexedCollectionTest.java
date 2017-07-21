package hr.fer.zemris.java.custom.collections;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayIndexedCollectionTest {

    /**
     * The array which will be tested.
     */
    ArrayIndexedCollection array = new ArrayIndexedCollection();

    /**
     * Set up the array since all methods rely on this existing structure.
     * This method will be call priod
     */
    @Before
    public void setUp() {
        array.clear();
        array.add(42);
        array.add(56);
        array.add(13);
        array.add(0);
        array.add(25);
    }

    @Test
    public void testAddingElementsToArray() {
        assertTrue(array.contains(42));
        assertTrue(array.contains(56));
        assertTrue(array.contains(13));
        assertTrue(array.contains(0));
        assertTrue(array.contains(25));
    }

    @Test
    public void testAddingDuplicatesToArray() {
        array.add(42);

        assertEquals(6, array.size());
    }

    @Test
    public void testAddingNullToArray() {
        boolean exceptionHappened = false;

        try {
            array.add(null);
        } catch (IllegalArgumentException ex) {
            exceptionHappened = true;
        }

        assertTrue(exceptionHappened);
    }

    @Test
    public void testGettingElementFromArray() {
        assertEquals(13, array.get(2));
    }

    @Test
    public void testGettingElementWithNegativeIndex() {
        boolean exceptionHappened = false;

        try {
            array.get(-1);
        } catch (IndexOutOfBoundsException ex) {
            exceptionHappened = true;
        }

        assertTrue(exceptionHappened);
    }

    @Test
    public void testGettingElementWithTooLargeIndex() {
        boolean exceptionHappened = false;

        try {
            array.get(9000);
        } catch (IndexOutOfBoundsException ex) {
            exceptionHappened = true;
        }

        assertTrue(exceptionHappened);
    }

    @Test
    public void testSizeOfTheArray() {
        assertEquals(5, array.size());
    }

    @Test
    public void testClearingTheArray() {
        array.clear();

        assertEquals(0, array.size());
    }

    @Test
    public void testInsertingElementIntoArray() {
        array.insert(750, 2);

        assertEquals(6, array.size());
        assertEquals(2, array.indexOf(750));
        assertEquals(5, array.indexOf(25));
    }

    @Test
    public void testInsertingElementWithNegativeIndexIntoArray() {
        boolean exceptionHappened = false;

        try {
            array.insert(87, -5);
        } catch (IllegalArgumentException ex) {
            exceptionHappened = true;
        }

        assertTrue(exceptionHappened);
    }

    @Test
    public void testIndexOfElement() {
        assertEquals(2, array.indexOf(13));
    }

    @Test
    public void testIndexOfNonExistingElement() {
        assertEquals(-1, array.indexOf(2017));
    }

    @Test
    public void testRemovingElementFromArray() {
        array.remove(2);

        assertEquals(4, array.size());
        assertFalse(array.contains(13));
    }

    @Test
    public void testRemovingNonExistingElement() {
        boolean exceptionHappened = false;
        try {
            array.remove(500);
        } catch (IllegalArgumentException ex) {
            exceptionHappened = true;
        }

        assertTrue(exceptionHappened);
    }
}