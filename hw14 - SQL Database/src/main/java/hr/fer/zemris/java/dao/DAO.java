package hr.fer.zemris.java.dao;

import hr.fer.zemris.java.model.Poll;
import hr.fer.zemris.java.model.PollOption;

import java.util.List;

/**
 * This class represents a service provider towards the data persistence
 * subsystem. The implementation of this interface must be able to provide
 * access to some type of subsystem or other persistence mechanism which
 * is in charge of data manipulation on the ground layer.
 *
 * @author Luka Čupić
 */
public interface DAO {


	/**
	 * Obtains a poll from the subsystem.
	 *
	 * @param id the ID of the poll to get
	 * @return the poll from the subsystem, specified with the given ID
	 * @throws DAOException if an error occurs while accessing the data
	 */
	Poll getPoll(int id) throws DAOException;

	/**
	 * Obtains a poll option from the subsystem.
	 *
	 * @param id the ID of the poll option to get
	 * @return the poll option from the subsystem, specified with the
	 * given ID
	 * @throws DAOException if an error occurs while accessing the data
	 */
	PollOption getPollOption(int id) throws DAOException;

	/**
	 * Obtains all polls from the subsystem.
	 *
	 * @return a list of {@link Poll} objects, each representing
	 * a single poll
	 * @throws DAOException if an error occurs while accessing the data
	 */
	List<Poll> getPolls() throws DAOException;

	/**
	 * Obtains all poll options from the subsystem.
	 *
	 * @return a list of {@link PollOption} objects, each representing
	 * a single poll option
	 * @throws DAOException if an error occurs while accessing the data
	 */
	List<PollOption> getPollOptions() throws DAOException;

	/**
	 * Increments the number of votes for the specified poll option by
	 * {@code 1}.
	 *
	 * @param id the ID of the poll option whose votes are to be updated
	 * @throws DAOException if an error occurs while accessing the data
	 */
	void incrementPollOptionsVote(int id) throws DAOException;
}