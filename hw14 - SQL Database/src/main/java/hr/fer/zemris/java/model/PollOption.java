package hr.fer.zemris.java.model;

/**
 * This class represents a voting poll option which encapsulates
 * the information about an n-tuple from the PollOptions table.
 *
 * @author Luka Čupić
 */
public class PollOption implements Comparable<PollOption> {

	/**
	 * The poll option ID.
	 */
	private int id;

	/**
	 * The poll option title.
	 */
	private String optionTitle;

	/**
	 * The poll option link.
	 */
	private String optionLink;

	/**
	 * The ID of the poll this option belongs to.
	 */
	private int pollID;

	/**
	 * The number of votes for this option.
	 */
	private int votesCount;

	/**
	 * Creates a new poll option.
	 *
	 * @param id          the poll option ID
	 * @param optionTitle the poll option title
	 * @param optionLink  the poll option link
	 * @param pollID      the ID of the poll
	 * @param votesCount  the number of votes
	 */
	public PollOption(int id, String optionTitle, String optionLink, int pollID, int votesCount) {
		this.id = id;
		this.optionTitle = optionTitle;
		this.optionLink = optionLink;
		this.pollID = pollID;
		this.votesCount = votesCount;
	}

	/**
	 * Gets the ID of the poll option.
	 *
	 * @return the ID of the poll option.
	 */
	public int getID() {
		return id;
	}

	/**
	 * Gets the title of the poll option.
	 *
	 * @return the title of the poll option.
	 */
	public String getOptionTitle() {
		return optionTitle;
	}

	/**
	 * Gets the link of the poll option.
	 *
	 * @return the link of the poll option.
	 */
	public String getOptionLink() {
		return optionLink;
	}

	/**
	 * Gets the ID of the poll.
	 *
	 * @return the ID of the poll.
	 */
	public int getPollID() {
		return pollID;
	}

	/**
	 * Gets the number of votes.
	 *
	 * @return the number of votes
	 */
	public int getVotesCount() {
		return votesCount;
	}

	@Override
	public int compareTo(PollOption o) {
		return Integer.compare(votesCount, o.getVotesCount());
	}
}
