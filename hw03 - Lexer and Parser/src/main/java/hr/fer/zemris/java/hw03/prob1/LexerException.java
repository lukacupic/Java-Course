package hr.fer.zemris.java.hw03.prob1;

/**
 * Thrown to indicate that an error occurred while performing lexical analysis.
 *
 * @author Luka Čupić
 */
public class LexerException extends RuntimeException {

	/**
	 * Default serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
     * The default class constructor.
     */
    public LexerException() {
        super();
    }

    /**
     * Creates a new exception with the description of the cause.
     *
     * @param message description of the exception's cause.
     */
    public LexerException(String message) {
        super(message);
    }
}
