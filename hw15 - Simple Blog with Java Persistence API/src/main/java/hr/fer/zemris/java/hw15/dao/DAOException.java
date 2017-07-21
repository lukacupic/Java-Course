package hr.fer.zemris.java.hw15.dao;

/**
 * Thrown to indicate that an error has occurred while accessing
 * the database or some other mechanism which is in charge of the
 * data persistence.
 *
 * @author Luka Čupić
 */
public class DAOException extends RuntimeException {

	/**
	 * Constructs a new {@link DAOException}.
	 */
	public DAOException() {
	}

	/**
	 * Constructs a {@link DAOException} with the specified detail message
	 * and cause.
	 *
	 * @param message the detail message of the exception
	 * @param cause   the cause of the exception
	 */
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a {@link DAOException} with the specified detail message.
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