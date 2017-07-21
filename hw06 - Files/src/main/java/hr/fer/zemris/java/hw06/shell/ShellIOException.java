package hr.fer.zemris.java.hw06.shell;

/**
 * Thrown to indicate that an error has occurred while working with {@link MyShell}.
 *
 * @author Luka Čupić
 */
public class ShellIOException extends RuntimeException {

    /**
     * The default constructor.
     */
    public ShellIOException() {
        super();
    }

    /**
     * Creates an instance of this exception with an appropriate message.
     *
     * @param message the message to append to this exception
     */
    public ShellIOException(String message) {
        super(message);
    }
}