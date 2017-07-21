package hr.fer.zemris.java.model;

/**
 * This class represents a voting poll which encapsulates
 * the information about an n-tuple from the Polls table.
 *
 * @author Luka Čupić
 */
public class Poll {

	/**
	 * The poll ID.
	 */
	private int id;

	/**
	 * The poll title.
	 */
	private String title;

	/**
	 * The poll message.
	 */
	private String message;

	/**
	 * Creates a new poll.
	 *
	 * @param id      the poll ID
	 * @param title   the poll title
	 * @param message the poll message
	 */
	public Poll(int id, String title, String message) {
		this.id = id;
		this.title = title;
		this.message = message;
	}

	/**
	 * Gets the poll ID.
	 *
	 * @return the poll ID.
	 */
	public int getID() {
		return id;
	}

	/**
	 * Gets the poll title.
	 *
	 * @return the poll title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Gets the poll message.
	 *
	 * @return the poll message
	 */
	public String getMessage() {
		return message;
	}
}
