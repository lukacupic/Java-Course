package hr.fer.zemris.java.hw04.db;

/**
 * Thrown to indicate that a parsing error has occurred.
 */
public class QueryParserException extends RuntimeException {

    /**
     * Default serial version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The default class constructor.
     */
    public QueryParserException() {
        super();
    }

    /**
     * Creates a new exception with the description of the cause.
     *
     * @param message description of the exception's cause.
     */
    public QueryParserException(String message) {
        super(message);
    }

    /**
     * Wraps an existing exception into SmartScriptParserException.
     *
     * @param exception the exception to wrap.
     */
    public QueryParserException(Exception exception) {
        super(exception);
    }
}
