package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Thrown to indicate that a parsing error has occurred.
 */
public class SmartScriptParserException extends RuntimeException {

    /**
     * Default serial version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The default class constructor.
     */
    public SmartScriptParserException() {
        super();
    }

    /**
     * Creates a new exception with the description of the cause.
     *
     * @param message description of the exception's cause.
     */
    public SmartScriptParserException(String message) {
        super(message);
    }

    /**
     * Wraps an existing exception into SmartScriptParserException.
     *
     * @param exception the exception to wrap.
     */
    public SmartScriptParserException(Exception exception) {
        super(exception);
    }
}