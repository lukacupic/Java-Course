package hr.fer.zemris.java.hw01;

import static hr.fer.zemris.java.hw01.UniqueNumbers.*;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class UniqueNumbersTest {
	
	/**
	 * The head of the tree.
	 */
	static TreeNode head = null;

	/**
	 * Sets up the tree with some arbitrary values. This method will be called
	 * only once: before any of the tests occur.
	 */
	@BeforeClass
	public static void setUp() {
		head = new TreeNode();
		head.value = 42;
		
		head.right = new TreeNode();
		head.right.value = 76;
		
		head.left = new TreeNode();
		head.left.value = 21;
		
		head.left.right = new TreeNode();
		head.left.right.value = 35;
		
		// the code above is equivalent to this one:
		/*
		head = addNode(head, 42);
		head = addNode(head, 76);
		head = addNode(head, 21);
		head = addNode(head, 35);
		*/
	}

	@Test
	public void sizeOfEmptyTree() {
		TreeNode head = null;
		
		int size = treeSize(head);
		assertEquals(size, 0);
	}
	
	@Test
	public void sizeOfNonEmptyTree() {
		int size = treeSize(head);
		assertEquals(size, 4);
	}
	
	@Test
	public void addAnExistingValueToTheTree() {
		// adds an existing value; the tree should just ignore it
		head = addNode(head, 76);
		
		int size = treeSize(head);
		assertEquals(size, 4);
	}
	
	@Test
	public void checkIfTreeHasAnExistingValue() {
		assertTrue(containsValue(head, 42));
	}
	
	@Test
	public void checkIfTreeHasANonExistingValue() {
		assertFalse(containsValue(head, 2017));
	}
	
	@Test
	public void addAnElementToAnEmptyTree() {
		TreeNode head = null;
		
		int value = 42;
		head = addNode(head, value);
		
		assertTrue(treeSize(head) == 1);
	}
	
	@Test
	public void checkIfTreeHierarchyIsCorrect() {
		assertTrue(head.value == 42);
		assertTrue(head.right.value == 76);
		assertTrue(head.left.value == 21);
		assertTrue(head.left.right.value == 35);
		
		 // check the non-existing elements too
		assertTrue(head.left.left == null);
		assertTrue(head.right.right == null);
	}
}
