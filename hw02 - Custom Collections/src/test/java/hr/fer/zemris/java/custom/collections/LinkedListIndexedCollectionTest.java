package hr.fer.zemris.java.custom.collections;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LinkedListIndexedCollectionTest {

    LinkedListIndexedCollection col = new LinkedListIndexedCollection();

    @Test
    public void addToTheList() {
        col.add(42);
        col.add(56);
        col.add(13);

        assertEquals(3, col.size());
    }

    @Test
    public void insertToTheList() {
        col.insert(42, 0);
        col.insert(56, 0);
        col.insert(13, 1);
        col.insert(700, 3);


        assertEquals(4, col.size());
    }

    @Test
    public void getFromTheList() {
        col.add(42);
        col.add(56);
        col.add(13);

        assertEquals(42, col.get(0));
    }

    @Test
    public void clearList() {
        col.add(42);
        col.add(56);
        col.add(13);

        col.clear();

        assertEquals(0, col.size());
    }

    @Test
    public void removeFromList() {
        col.add(42);
        col.add(56);
        col.add(13);

        col.remove(2);
        assertEquals(2, col.size());

        col.remove(1);
        assertEquals(1, col.size());

        col.remove(0);
        assertEquals(0, col.size());
    }
}