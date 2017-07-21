package hr.fer.zemris.java.custom.collections;

/**
 * Thrown to indicate that an invalid operation has been attempted on an empty stack.
 *
 * @author Luka Čupić
 */
public class EmptyStackException extends RuntimeException {

	/**
	 * Default serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
     * The default constructor.
     */
    public EmptyStackException() {
        super();
    }
}
