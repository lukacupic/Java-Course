package hr.fer.zemris.bf.lexer;

/**
 * Thrown to indicate that an error has occurred while a {@link Lexer}
 * object was performing a lexical analysis on the given sequence.
 *
 * @author Luka Čupić
 */
public class LexerException extends RuntimeException {

    /**
     * Creates a new instance of this exception.
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
