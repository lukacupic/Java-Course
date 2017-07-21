package hr.fer.zemris.bf.parser;

/**
 * Thrown to indicate that an error has occurred while a {@link Parser} object
 * was parsing the given expression.
 *
 * @author Luka Čupić
 */
public class ParserException extends RuntimeException {

    /**
     * Creates a new instance of this exception.
     */
    public ParserException() {
        super();
    }

    /**
     * Creates a new exception with the description of the cause.
     *
     * @param message description of the cause.
     */
    public ParserException(String message) {
        super(message);
    }

    /**
     * Wraps an existing exception into this exception..
     *
     * @param exception the exception to wrap.
     */
    public ParserException(Exception exception) {
        super(exception);
    }
}
