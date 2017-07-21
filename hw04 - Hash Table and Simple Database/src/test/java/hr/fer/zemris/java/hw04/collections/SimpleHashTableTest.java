package hr.fer.zemris.java.hw04.collections;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class SimpleHashTableTest {

    private SimpleHashTable<String, Integer> map = new SimpleHashTable<>(2);

    @Before
    public void setUp() {
        map.put("Ivana", 13);
        map.put("Ante", 42);
        map.put("Jasna", 56);
        map.put("Kristina", 89);
    }

    @Test
    public void testHashTableSize() {
        assertEquals(4, map.size());
    }

    @Test
    public void getElementWithCertainKey() {
        assertEquals(42, (int) map.get("Ante"));
        assertEquals(56, (int) map.get("Jasna"));
    }

    @Test
    public void testIfTableContainsExistingValues() {
        assertTrue(map.containsValue(13));
        assertTrue(map.containsValue(42));
        assertTrue(map.containsValue(56));
        assertTrue(map.containsValue(89));
    }

    @Test
    public void testIfTableContainsNonExistingValues() {
        assertFalse(map.containsValue(-14));
        assertFalse(map.containsValue(73));
        assertFalse(map.containsValue(66));
        assertFalse(map.containsValue(1024));
    }

    @Test
    public void testRemovingAnEntry() {
        map.remove("Jasna");

        assertFalse(map.containsKey("Jasna"));
        assertEquals(3, map.size());
    }

    @Test
    public void testClearingTheMap() {
        map.clear();

        assertEquals(0, map.size());
    }

    @Test(expected = NoSuchElementException.class)
    public void testIteratingTheMapWithoutCallingHasNextMethod() {
        Iterator<SimpleHashTable.TableEntry<String, Integer>> it = map.iterator();

        while (true) {
            it.next();
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testRemovingAnEntryMultipleTimes() {
        Iterator<SimpleHashTable.TableEntry<String, Integer>> iter = map.iterator();
        while (iter.hasNext()) {
            SimpleHashTable.TableEntry<String, Integer> pair = iter.next();
            if (pair.getKey().equals("Ivana")) {
                iter.remove();
                iter.remove();
            }
        }
    }
}
