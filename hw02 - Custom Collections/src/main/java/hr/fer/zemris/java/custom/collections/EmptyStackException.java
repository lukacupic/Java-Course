package hr.fer.zemris.java.custom.collections;

/**
 * Thrown to indicate that an invalid operation has been attempted on an empty stack.
 * 
 * @author Luka Čupić
 *
 */
public class EmptyStackException extends RuntimeException {
	
	public EmptyStackException() {
		super();
	}
}
