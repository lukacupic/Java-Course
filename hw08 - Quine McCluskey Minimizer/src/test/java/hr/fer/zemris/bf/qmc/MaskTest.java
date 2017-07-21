package hr.fer.zemris.bf.qmc;

import org.junit.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.*;

public class MaskTest {

    @Test(expected = IllegalArgumentException.class)
    public void testCreatingANewMaskWithIllegalIndex() {
        new Mask(-1, 4, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatingANewMaskWithIllegalNumberOfVariables() {
        new Mask(5, -42, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatingANewMaskWithTooLargeIndex() {
        new Mask(5892, 4, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatingANewMaskWithNullMaskValues() {
        Set<Integer> indexes = new TreeSet<>();
        indexes.add(2);
        indexes.add(3);

        new Mask(null, indexes, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatingANewMaskWithNullSetOfIndexes() {
        new Mask(new byte[]{0, 0, 1, 2}, null, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatingANewMaskWithEmptySetOfIndexes() {
        Set<Integer> indexes = new TreeSet<>();
        new Mask(new byte[]{0, 0, 1, 2}, indexes, true);
    }

    @Test
    public void testCountingOnes() {
        Mask mask = new Mask(3, 4, false);

        assertEquals(2, mask.countOfOnes());
    }

    @Test
    public void testIfSizeReturnsActualSize() {
        Mask mask = new Mask(3, 4, false);

        assertEquals(4, mask.size());
    }

    @Test
    public void testIfGetValueAtReturnsProperValues1() {
        Mask mask = new Mask(3, 4, false);

        assertEquals(0, mask.getValueAt(0));
        assertEquals(0, mask.getValueAt(1));
        assertEquals(1, mask.getValueAt(2));
        assertEquals(1, mask.getValueAt(3));
    }

    @Test
    public void testIfGetValueAtReturnsProperValues2() {
        Set<Integer> indexes = new TreeSet<>();
        indexes.add(2);
        indexes.add(3);

        Mask mask = new Mask(new byte[]{0, 0, 1, 2}, indexes, true);

        assertEquals(0, mask.getValueAt(0));
        assertEquals(0, mask.getValueAt(1));
        assertEquals(1, mask.getValueAt(2));
        assertEquals(2, mask.getValueAt(3));
    }

    @Test
    public void testCombiningAMaskWithAnotherMask() {
        Set<Integer> s1 = new HashSet<>();
        s1.add(8);
        s1.add(10);
        Mask mask1 = new Mask(new byte[]{1, 0, 2, 0}, s1, true);

        Set<Integer> s2 = new HashSet<>();
        s2.add(9);
        s2.add(11);
        Mask mask2 = new Mask(new byte[]{1, 0, 2, 1}, s2, true);

        Optional<Mask> optional = mask1.combineWith(mask2);

        if (!optional.isPresent()) {
            fail();
        }

        Mask combined = optional.get();

        assertEquals(1, combined.getValueAt(0));
        assertEquals(0, combined.getValueAt(1));
        assertEquals(2, combined.getValueAt(2));
        assertEquals(2, combined.getValueAt(3));

        Set<Integer> combinedIndexes = combined.getIndexes();

        assertEquals(4, combinedIndexes.size());

        assertTrue(combinedIndexes.contains(8));
        assertTrue(combinedIndexes.contains(10));
        assertTrue(combinedIndexes.contains(9));
        assertTrue(combinedIndexes.contains(11));

        assertTrue(combined.isDontCare());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCombiningMasksOfDifferentLengths() {
        Set<Integer> s1 = new HashSet<>();
        s1.add(0);
        Mask mask1 = new Mask(new byte[]{0, 0, 0}, s1, false);

        Set<Integer> s2 = new HashSet<>();
        s2.add(31);
        Mask mask2 = new Mask(new byte[]{1, 1, 1, 1, 1}, s2, false);

        mask1.combineWith(mask2); // throws!
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCombiningMaskWithNull() {
        Set<Integer> s1 = new HashSet<>();
        s1.add(0);
        Mask mask1 = new Mask(new byte[]{0, 0, 0}, s1, false);

        mask1.combineWith(null); // throws!
    }

    @Test
    public void testCombiningMasksWhichDifferOnMultiplePositions() {
        Set<Integer> s1 = new HashSet<>();
        s1.add(5);
        Mask mask1 = new Mask(new byte[]{0, 1, 0, 1}, s1, false);

        Set<Integer> s2 = new HashSet<>();
        s2.add(10);
        Mask mask2 = new Mask(new byte[]{1, 0, 1, 0}, s2, false);

        assertFalse(mask1.combineWith(mask2).isPresent());
    }

    @Test
    public void testIfHashCodesOfMaskWithTheSameValuesAreEqual() {
        Set<Integer> indexes = new HashSet<>();
        indexes.add(42);

        Mask mask1 = new Mask(new byte[]{1, 0, 1, 0, 1, 0}, indexes, false);
        Mask mask2 = new Mask(new byte[]{1, 0, 1, 0, 1, 0}, indexes, false);

        assertEquals(mask1.hashCode(), mask2.hashCode());
    }

    @Test
    public void testIfMaskWithTheSameValuesAreEqual() {
        Set<Integer> indexes = new HashSet<>();
        indexes.add(42);

        Mask mask1 = new Mask(new byte[]{1, 0, 1, 0, 1, 0}, indexes, false);
        Mask mask2 = new Mask(new byte[]{1, 0, 1, 0, 1, 0}, indexes, false);

        assertTrue(mask1.equals(mask2));
    }

    @Test
    public void testIfMaskWithTheDifferentValuesAreNotEqual() {
        Set<Integer> s1 = new HashSet<>();
        s1.add(42);
        Mask mask1 = new Mask(new byte[]{1, 0, 1, 0, 1, 0}, s1, false);

        Set<Integer> s2 = new HashSet<>();
        s2.add(21);
        Mask mask2 = new Mask(new byte[]{0, 1, 0, 1, 0, 1}, s2, false);

        assertFalse(mask1.equals(mask2));
    }

    @Test
    public void testIfToStringReturnsProperValue() {
        Set<Integer> indexes = new HashSet<>();
        indexes.add(10);
        indexes.add(11);

        Mask mask = new Mask(new byte[]{1, 0, 1, 2}, indexes, true);

        assertEquals("101- D   [10, 11]", mask.toString());
    }
}