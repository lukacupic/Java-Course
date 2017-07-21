package hr.fer.zemris.java.custom.scripting.exec;

import org.junit.Test;

import javax.swing.*;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ValueWrapperTest {

    // ADD TESTS

    @Test
    public void testAddingIntegers() {
        ValueWrapper wrapper = new ValueWrapper(52);
        wrapper.add(10);

        assertEquals(62, wrapper.getValue());
    }

    @Test
    public void testAddingIntAndDouble() {
        ValueWrapper wrapper = new ValueWrapper(52);
        wrapper.add(3.14);

        assertEquals(55.14, wrapper.getValue());
    }

    @Test
    public void testAddingDoublesButCreatedAsStrings() {
        ValueWrapper wrapper = new ValueWrapper("3.14");
        wrapper.add("2.72");

        assertEquals(5.86, wrapper.getValue());
    }

    @Test
    public void testAddingIntsButCreatedAsStrings() {
        ValueWrapper wrapper = new ValueWrapper("5");
        wrapper.add("42");

        assertEquals(47, wrapper.getValue());
    }

    @Test
    public void testAddingNulls() {
        ValueWrapper wrapper = new ValueWrapper(null);
        wrapper.add(null);

        assertEquals(0, wrapper.getValue());
    }

    @Test(expected = RuntimeException.class)
    public void testAddingIllegalValues() {
        ValueWrapper wrapper = new ValueWrapper(new Scanner(System.in));
        wrapper.add(new JFrame());
    }

    @Test(expected = RuntimeException.class)
    public void testAddingIllegalValues2() {
        ValueWrapper wrapper = new ValueWrapper(42);
        wrapper.add(new JFrame());
    }

    @Test(expected = RuntimeException.class)
    public void testAddingIllegalValues3() {
        ValueWrapper wrapper = new ValueWrapper(new Thread());
        wrapper.add(42);
    }

    @Test(expected = RuntimeException.class)
    public void testAddingIllegalValuesAsStrings() {
        ValueWrapper wrapper = new ValueWrapper("42");
        wrapper.add("not legal!");
    }

    @Test(expected = RuntimeException.class)
    public void testCreatingALegalWrapperButPerformingAnIllegalAddingOperation() {
        ValueWrapper wrapper = new ValueWrapper("this is okay here");
        assertTrue(wrapper.getValue() instanceof String);

        wrapper.add(42); // but this throws!
    }

    // SUBTRACT TESTS

    @Test
    public void testSubtractingIntegers() {
        ValueWrapper wrapper = new ValueWrapper(52);
        wrapper.subtract(10);

        assertEquals(42, wrapper.getValue());
    }

    @Test
    public void testSubtractingDoubleFromInt() {
        ValueWrapper wrapper = new ValueWrapper(52);
        wrapper.subtract(3.14);

        assertEquals(48.86, wrapper.getValue());
    }

    @Test
    public void testSubtractingDoublesButCreatedAsStrings() {
        ValueWrapper wrapper = new ValueWrapper("3.14");
        wrapper.subtract("2.72");

        assertEquals(0.42, (double) wrapper.getValue(), 10E-5);
    }

    @Test
    public void testSubtractingIntsButCreatedAsStrings() {
        ValueWrapper wrapper = new ValueWrapper("42");
        wrapper.subtract("5");

        assertEquals(37, wrapper.getValue());
    }

    @Test
    public void testSubtractingNulls() {
        ValueWrapper wrapper = new ValueWrapper(null);
        wrapper.subtract(null);

        assertEquals(0, wrapper.getValue());
    }

    @Test(expected = RuntimeException.class)
    public void testSubtractingIllegalValues() {
        ValueWrapper wrapper = new ValueWrapper(new Scanner(System.in));
        wrapper.subtract(new JFrame());
    }

    @Test(expected = RuntimeException.class)
    public void testSubtractingIllegalValues2() {
        ValueWrapper wrapper = new ValueWrapper(42);
        wrapper.subtract(new JFrame());
    }

    @Test(expected = RuntimeException.class)
    public void testSubtractingIllegalValues3() {
        ValueWrapper wrapper = new ValueWrapper(new Thread());
        wrapper.subtract(42);
    }

    @Test(expected = RuntimeException.class)
    public void testSubtractingIllegalValuesAsStrings() {
        ValueWrapper wrapper = new ValueWrapper("42");
        wrapper.subtract("not legal!");
    }

    @Test(expected = RuntimeException.class)
    public void testCreatingALegalWrapperButPerformingAnIllegalSubtractOperation() {
        ValueWrapper wrapper = new ValueWrapper("this is okay here");
        assertTrue(wrapper.getValue() instanceof String);

        wrapper.subtract(42); // but this throws!
    }

    // MULTIPLY TESTS

    @Test
    public void testMultiplyingIntegers() {
        ValueWrapper wrapper = new ValueWrapper(52);
        wrapper.multiply(10);

        assertEquals(520, wrapper.getValue());
    }

    @Test
    public void testMultiplyingIntAndDouble() {
        ValueWrapper wrapper = new ValueWrapper(52);
        wrapper.multiply(3.14);

        assertEquals(163.28, (double) wrapper.getValue(), 10E-5);
    }

    @Test
    public void testMultiplyingDoublesButCreatedAsStrings() {
        ValueWrapper wrapper = new ValueWrapper("3.14");
        wrapper.multiply("2.72");

        assertEquals(8.5408, (double) wrapper.getValue(), 10E-5);
    }

    @Test
    public void testMultiplyingIntsButCreatedAsStrings() {
        ValueWrapper wrapper = new ValueWrapper("5");
        wrapper.multiply("42");

        assertEquals(210, wrapper.getValue());
    }

    @Test
    public void testMultiplyingNulls() {
        ValueWrapper wrapper = new ValueWrapper(null);
        wrapper.multiply(null);

        assertEquals(0, wrapper.getValue());
    }

    @Test(expected = RuntimeException.class)
    public void testMultiplyingIllegalValues() {
        ValueWrapper wrapper = new ValueWrapper(new Scanner(System.in));
        wrapper.multiply(new JFrame());
    }

    @Test(expected = RuntimeException.class)
    public void testMultiplyingIllegalValues2() {
        ValueWrapper wrapper = new ValueWrapper(42);
        wrapper.multiply(new JFrame());
    }

    @Test(expected = RuntimeException.class)
    public void testMultiplyingIllegalValues3() {
        ValueWrapper wrapper = new ValueWrapper(new Thread());
        wrapper.multiply(42);
    }

    @Test(expected = RuntimeException.class)
    public void testMultiplyingIllegalValuesAsStrings() {
        ValueWrapper wrapper = new ValueWrapper("42");
        wrapper.multiply("not legal!");
    }

    @Test(expected = RuntimeException.class)
    public void testCreatingALegalWrapperButPerformingAnIllegalMultiplyingOperation() {
        ValueWrapper wrapper = new ValueWrapper("this is okay here");
        assertTrue(wrapper.getValue() instanceof String);

        wrapper.multiply(42); // but this throws!
    }

    // DIVIDE TESTS

    @Test
    public void testDividingIntegers() {
        ValueWrapper wrapper = new ValueWrapper(52);
        wrapper.divide(10);

        assertEquals(5.2, (double) wrapper.getValue(), 10E-5);
    }

    @Test
    public void testDividingIntAndDouble() {
        ValueWrapper wrapper = new ValueWrapper(52);
        wrapper.divide(3.14);

        assertEquals(16.5605096, (double) wrapper.getValue(), 10E-5);
    }

    @Test
    public void testDividingDoublesButCreatedAsStrings() {
        ValueWrapper wrapper = new ValueWrapper("3.14");
        wrapper.divide("2.72");

        assertEquals(1.15441176, (double) wrapper.getValue(), 10E-5);
    }

    @Test
    public void testDividingIntsButCreatedAsStrings() {
        ValueWrapper wrapper = new ValueWrapper("50");
        wrapper.divide("5");

        assertEquals(10, (double) wrapper.getValue(), 10E-5);
    }

    @Test
    public void testDividingNulls() {
        ValueWrapper wrapper = new ValueWrapper(null);
        wrapper.divide(null);

        assertEquals(Double.NaN, wrapper.getValue());
    }

    @Test(expected = RuntimeException.class)
    public void testDividingIllegalValues() {
        ValueWrapper wrapper = new ValueWrapper(new Scanner(System.in));
        wrapper.divide(new JFrame());
    }

    @Test(expected = RuntimeException.class)
    public void testDividingIllegalValues2() {
        ValueWrapper wrapper = new ValueWrapper(42);
        wrapper.divide(new JFrame());
    }

    @Test(expected = RuntimeException.class)
    public void testDividingIllegalValues3() {
        ValueWrapper wrapper = new ValueWrapper(new Thread());
        wrapper.divide(42);
    }

    @Test(expected = RuntimeException.class)
    public void testDividingIllegalValuesAsStrings() {
        ValueWrapper wrapper = new ValueWrapper("42");
        wrapper.divide("not legal!");
    }

    @Test(expected = RuntimeException.class)
    public void testCreatingALegalWrapperButPerformingAnIllegalDividingOperation() {
        ValueWrapper wrapper = new ValueWrapper("this is okay here");
        assertTrue(wrapper.getValue() instanceof String);

        wrapper.divide(42); // but this throws!
    }

    // COMPARE TESTS

    @Test
    public void testComparingIntegers() {
        ValueWrapper wrapper = new ValueWrapper(52);

        int result = wrapper.numCompare(10);
        assertTrue(result > 0);
    }

    @Test
    public void testComparingIntAndDouble() {
        ValueWrapper wrapper = new ValueWrapper(52);

        int result = wrapper.numCompare(3.14);
        assertTrue(result > 0);
    }

    @Test
    public void testComparingDoublesButCreatedAsStrings() {
        ValueWrapper wrapper = new ValueWrapper("2.72");

        int result = wrapper.numCompare("3.14");
        assertTrue(result < 0);
    }

    @Test
    public void testComparingIntsButCreatedAsStrings() {
        ValueWrapper wrapper = new ValueWrapper("50");

        int result = wrapper.numCompare("50");
        assertTrue(result == 0);
    }

    @Test
    public void testComparingNulls() {
        ValueWrapper wrapper = new ValueWrapper(null);

        int result = wrapper.numCompare(null);
        assertTrue(result == 0);
    }

    @Test(expected = RuntimeException.class)
    public void testComparingIllegalValues() {
        ValueWrapper wrapper = new ValueWrapper(new Scanner(System.in));
        wrapper.numCompare(new JFrame());
    }

    @Test(expected = RuntimeException.class)
    public void testComparingIllegalValues2() {
        ValueWrapper wrapper = new ValueWrapper(42);
        wrapper.numCompare(new JFrame());
    }

    @Test(expected = RuntimeException.class)
    public void testComparingIllegalValues3() {
        ValueWrapper wrapper = new ValueWrapper(new Thread());
        wrapper.numCompare(42);
    }

    @Test(expected = RuntimeException.class)
    public void testComparingIllegalValuesAsStrings() {
        ValueWrapper wrapper = new ValueWrapper("42");
        wrapper.numCompare("not legal!");
    }

    @Test(expected = RuntimeException.class)
    public void testCreatingALegalWrapperButPerformingAnIllegalComparingOperation() {
        ValueWrapper wrapper = new ValueWrapper("this is okay here");
        assertTrue(wrapper.getValue() instanceof String);

        wrapper.numCompare(42); // but this throws!
    }
}