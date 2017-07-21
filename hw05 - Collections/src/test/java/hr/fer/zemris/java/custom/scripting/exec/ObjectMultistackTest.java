package hr.fer.zemris.java.custom.scripting.exec;

import org.junit.Test;

import java.util.EmptyStackException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ObjectMultistackTest {

    @Test
    public void testPushingAndPoppingFromStack() {
        ObjectMultistack multistack = new ObjectMultistack();

        multistack.push("Stack1", new ValueWrapper(42));
        multistack.push("Stack1", new ValueWrapper(43));
        multistack.push("Stack1", new ValueWrapper(44));

        assertEquals(44, multistack.pop("Stack1").getValue());
        assertEquals(43, multistack.pop("Stack1").getValue());
        assertEquals(42, multistack.pop("Stack1").getValue());
    }

    @Test
    public void testPushingAndPoppingFromMultipleStacks() {
        ObjectMultistack multistack = new ObjectMultistack();

        multistack.push("Odd stack", new ValueWrapper(1));
        multistack.push("Even stack", new ValueWrapper(2));
        multistack.push("Odd stack", new ValueWrapper(3));
        multistack.push("Even stack", new ValueWrapper(4));
        multistack.push("Odd stack", new ValueWrapper(5));
        multistack.push("Even stack", new ValueWrapper(6));

        assertEquals(6, multistack.pop("Even stack").getValue());
        assertEquals(5, multistack.pop("Odd stack").getValue());
        assertEquals(4, multistack.pop("Even stack").getValue());
        assertEquals(3, multistack.pop("Odd stack").getValue());
        assertEquals(2, multistack.pop("Even stack").getValue());
        assertEquals(1, multistack.pop("Odd stack").getValue());
    }

    @Test
    public void testIfAnEmptyStackIsEmpty() {
        ObjectMultistack multistack = new ObjectMultistack();

        multistack.push("Stack", new ValueWrapper(1));
        multistack.push("Stack", new ValueWrapper(2));
        multistack.push("Stack", new ValueWrapper(3));

        multistack.pop("Stack");
        multistack.pop("Stack");
        multistack.pop("Stack");

        assertTrue(multistack.isEmpty("Stack"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIfAnNonExistingStackIsEmpty() {
        ObjectMultistack multistack = new ObjectMultistack();

        assertTrue(multistack.isEmpty("Non-existing stack"));  // throws!
    }

    @Test(expected = EmptyStackException.class)
    public void testPoppingAValueFromAnEmptyStack() {
        ObjectMultistack multistack = new ObjectMultistack();
        multistack.push("Empty stack", new ValueWrapper(null));
        multistack.pop("Empty stack");

        multistack.pop("Empty stack"); // throws!
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPoppingAValueFromANonExistingStack() {
        ObjectMultistack multistack = new ObjectMultistack();

        multistack.pop("Non-existing stack");  // throws!
    }

    @Test(expected = EmptyStackException.class)
    public void testPeekingAValueFromAnEmptyStack() {
        ObjectMultistack multistack = new ObjectMultistack();
        multistack.push("Empty stack", new ValueWrapper(null));
        multistack.pop("Empty stack");

        multistack.peek("Empty stack");  // throws!
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPeekingAValueFromANonExistingStack() {
        ObjectMultistack multistack = new ObjectMultistack();
        multistack.pop("Non-existing stack");

        multistack.peek("Non-existing stack");  // throws!
    }
}