package hr.fer.zemris.java.hw01;

import org.junit.Test;
import static org.junit.Assert.*;

public class FactorialTest {
	
	@Test
	public void factorialOfOne() {
		long fact = Factorial.factorial(1);
		assertEquals(fact, 1);
	}

	@Test
	public void factorialOfFive() {
		long fact = Factorial.factorial(5);
		assertEquals(fact, 120);
	}
	
	@Test
	public void factorialOfFourteen() {
		long fact = Factorial.factorial(14);
		assertEquals(fact, 87178291200L);
	}
	
	@Test
	public void factorialOfANegativeNumber() {
		long fact = Factorial.factorial(-1);
		assertEquals(fact, -1);
	}
	
	@Test
	public void factorialOfAHugeNumber() {
		long fact = Factorial.factorial(2017);
		assertEquals(fact, -1);
	}
}
