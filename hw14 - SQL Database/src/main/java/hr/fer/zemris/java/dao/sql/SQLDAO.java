package hr.fer.zemris.java.dao.sql;

import hr.fer.zemris.java.dao.DAO;
import hr.fer.zemris.java.dao.DAOException;
import hr.fer.zemris.java.model.Poll;
import hr.fer.zemris.java.model.PollOption;
import hr.fer.zemris.java.util.SQLCommands;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a DAO implementation using the technology SQL.
 * This specific implementation expects to have e connection throug the
 * {@link SQLConnectionProvider}, which means that the connection must
 * be set prior to using this class.
 *
 * @author Luka Čupić
 */
public class SQLDAO implements DAO {

	@Override
	public Poll getPoll(int id) throws DAOException {
		try {
			Connection con = SQLConnectionProvider.getConnection();
			PreparedStatement pst = con.prepareStatement(SQLCommands.GET_POLL);
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				String title = rs.getString(2);
				Clob clob = rs.getClob(3);
				String message = clob.getSubString(1, (int) clob.length());
				return new Poll(id, title, message);
			}
			return null;
		} catch (Exception ex) {
			throw new DAOException(ex);
		}
	}

	@Override
	public PollOption getPollOption(int id) throws DAOException {
		try {
			Connection con = SQLConnectionProvider.getConnection();
			PreparedStatement pst = con.prepareStatement(SQLCommands.GET_POLL_OPTION);
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				String optionTitle = rs.getString(2);
				String optionLink = rs.getString(3);
				int pollID = rs.getInt(4);
				int votesCount = rs.getInt(5);

				return new PollOption(id, optionTitle, optionLink, pollID, votesCount);
			}
			return null;
		} catch (Exception ex) {
			throw new DAOException(ex);
		}
	}

	@Override
	public List<Poll> getPolls() throws DAOException {
		try {
			Connection con = SQLConnectionProvider.getConnection();
			PreparedStatement pst = con.prepareStatement(SQLCommands.GET_POLLS);
			ResultSet rs = pst.executeQuery();

			List<Poll> polls = new ArrayList<>();
			while (rs.next()) {
				int id = rs.getInt(1);
				String title = rs.getString(2);
				Clob clob = rs.getClob(3);
				String message = clob.getSubString(1, (int) clob.length());

				polls.add(new Poll(id, title, message));
			}
			return polls;
		} catch (Exception ex) {
			throw new DAOException(ex);
		}
	}

	@Override
	public List<PollOption> getPollOptions() throws DAOException {
		try {
			Connection con = SQLConnectionProvider.getConnection();
			PreparedStatement pst = con.prepareStatement(SQLCommands.GET_POLL_OPTIONS);
			ResultSet rs = pst.executeQuery();

			List<PollOption> polls = new ArrayList<>();
			while (rs.next()) {
				int id = rs.getInt(1);
				String optionTitle = rs.getString(2);
				String optionLink = rs.getString(3);
				int pollID = rs.getInt(4);
				int votesCount = rs.getInt(5);

				polls.add(new PollOption(id, optionTitle, optionLink, pollID, votesCount));
			}
			return polls;
		} catch (Exception ex) {
			throw new DAOException(ex);
		}
	}

	@Override
	public void incrementPollOptionsVote(int id) throws DAOException {
		try {
			Connection con = SQLConnectionProvider.getConnection();
			PreparedStatement pst = con.prepareStatement(SQLCommands.INCREMENT_VOTES);
			pst.setInt(1, id);
			pst.executeUpdate();
		} catch (Exception ex) {
			throw new DAOException();
		}
	}
}