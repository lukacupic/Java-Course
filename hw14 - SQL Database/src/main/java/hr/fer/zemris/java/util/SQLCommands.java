package hr.fer.zemris.java.util;

/**
 * This class provides access to all SQL commands used during the runtime
 * of the web application.
 *
 * @author Luka Čupić
 */
public class SQLCommands {

	/**
	 * An SQL command for creating the {@code Polls} relation.
	 */
	public static final String CREATE_POLLS = "CREATE TABLE Polls" +
		" (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
		" title VARCHAR(150) NOT NULL," +
		" message CLOB(2048) NOT NULL)";

	/**
	 * An SQL command for creating the {@code PollOptions} relation.
	 */
	public static final String CREATE_POLL_OPTIONS = "CREATE TABLE PollOptions" +
		" (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
		" optionTitle VARCHAR(100) NOT NULL," +
		" optionLink VARCHAR(150) NOT NULL," +
		" pollID BIGINT," +
		" votesCount BIGINT," +
		" FOREIGN KEY (pollID) REFERENCES Polls(id))";

	/**
	 * An SQL command for creating the Bands poll.
	 */
	public static final String CREATE_BANDS_POLL = "INSERT INTO Polls(title, message) " +
		"VALUES ('Glasanje za omiljeni bend', 'Od sljedećih bendova, koji Vam je bend " +
		"najdraži? Kliknite na link kako biste glasali!')";

	/**
	 * An SQL command for creating the programming poll.
	 */
	public static final String CREATE_PROGRAMMING_POLL = "INSERT INTO Polls(title, message) " +
		"VALUES ('Glasanje za najdraži jezik', 'Od sljedećih jezika, koji Vam je jezik " +
		"najdraži? Kliknite na link kako biste glasali!')";

	/**
	 * An SQL command which counts the number of polls.
	 */
	public static final String COUNT_POLLS = "SELECT COUNT(*) FROM Polls";

	/**
	 * An SQL command which returns a single poll from the {@code Polls} relation,
	 * using the ID as the index.
	 */
	public static final String GET_POLL = "SELECT * FROM Polls WHERE id=?";

	/**
	 * An SQL command which returns all polls from the {@code Polls} relation.
	 */
	public static final String GET_POLLS = "SELECT * FROM Polls";

	/**
	 * An SQL command which returns a single poll option from the {@code PollOptions}
	 * relation, using the ID as the index.
	 */
	public static final String GET_POLL_OPTION = "SELECT * FROM PollOptions WHERE id=?";

	/**
	 * An SQL command which returns all poll options from the {@code PollOptions}
	 * relation.
	 */
	public static final String GET_POLL_OPTIONS = "SELECT * FROM PollOptions";

	/**
	 * An SQL command which increments the number of votes for a given PollOption,
	 * specified by it's ID.
	 */
	public static final String INCREMENT_VOTES = "UPDATE PollOptions SET " +
		"votesCount = votesCount + 1 WHERE id = ?";

	/**
	 * A common beginning of an SQL command for inserting an n-tuple into the
	 * {@code PollOptions} relation.
	 */
	private static final String INSERT_INTO_POLL_OPTIONS = "INSERT INTO PollOptions" +
		"(optionTitle, optionLink, pollID, votesCount) VALUES ";

	/**
	 * An SQL command which adds some bands to the {@code PollOptions} relation.
	 */
	public static final String[] ADD_BANDS = {
		INSERT_INTO_POLL_OPTIONS + "('The Beatles', 'https://www.youtube.com/watch?v=z9ypq6_5bsg', ?, 42)",
		INSERT_INTO_POLL_OPTIONS + "('The Platters', 'https://www.youtube.com/watch?v=H2di83WAOhU', ?, 14)",
		INSERT_INTO_POLL_OPTIONS + "('The Beach Boys', 'https://www.youtube.com/watch?v=2s4slliAtQU', ?, 55)",
		INSERT_INTO_POLL_OPTIONS + "('The Four Seasons', 'https://www.youtube.com/watch?v=y8yvnqHmFds', ?, 6)",
		INSERT_INTO_POLL_OPTIONS + "('The Marcels', 'https://www.youtube.com/watch?v=qoi3TH59ZEs', ?, 5)",
		INSERT_INTO_POLL_OPTIONS + "('The Everly Brothers', 'https://www.youtube.com/watch?v=tbU3zdAgiX8', ?, 1)",
		INSERT_INTO_POLL_OPTIONS + "('The Mamas And The Papas', 'https://www.youtube.com/watch?v=N-aK6JnyFmk', ?, 12)",
	};

	/**
	 * An SQL command which adds some programming languages to the {@code PollOptions}
	 * relation.
	 */
	public static final String[] ADD_LANGUAGES = {
		INSERT_INTO_POLL_OPTIONS + "('Java', 'https://en.wikipedia.org/wiki/Java_(programming_language)', ?, 9000)",
		INSERT_INTO_POLL_OPTIONS + "('C++', 'https://en.wikipedia.org/wiki/C%2B%2B', ?, 42)",
		INSERT_INTO_POLL_OPTIONS + "('Assembler', 'https://en.wikipedia.org/wiki/Assembly_language', ?, 13)",
	};
}
