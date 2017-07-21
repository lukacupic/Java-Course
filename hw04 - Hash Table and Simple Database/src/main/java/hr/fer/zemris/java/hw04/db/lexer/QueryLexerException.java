package hr.fer.zemris.java.hw04.db.lexer;

/**
 * Thrown to indicate that an error occurred while performing lexical analysis.
 *
 * @author Luka Čupić
 */
public class QueryLexerException extends RuntimeException {

    /**
     * Default serial version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The default class constructor.
     */
    public QueryLexerException() {
        super();
    }

    /**
     * Creates a new exception with the description of the cause.
     *
     * @param message description of the exception's cause.
     */
    public QueryLexerException(String message) {
        super(message);
    }

    /**
     * Wraps an existing runtime exception into an instance of this exception.
     *
     * @param ex the exception to wrap.
     */
    public QueryLexerException(RuntimeException ex) {
        super(ex);
    }
}
