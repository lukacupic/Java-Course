package hr.fer.zemris.java.dao;

/**
 * Thrown to indicate that an error has occurred while accessing
 * the database or some other persistent data.
 *
 * @author Luka Čupić
 */
public class DAOException extends RuntimeException {

	/**
	 * Constructs a {@link DAOException}.
	 */

	public DAOException() {
	}

	/**
	 * Constructs an {@link DAOException} with the specified detail message
	 * and cause.
	 *
	 * @param message the detail message of the exception
	 * @param cause   the cause of the exception
	 */
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs an {@link DAOException} with the specified detail message.
	 *
	 * @param message the detail message of the exception
	 */
	public DAOException(String message) {
		super(message);
	}

	/**
	 * Constructs a {@link DAOException} with the specified cause.
	 *
	 * @param cause the cause of the exception
	 */
	public DAOException(Throwable cause) {
		super(cause);
	}
}