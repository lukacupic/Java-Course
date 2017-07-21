package hr.fer.zemris.java.hw15.dao.jpa;

import hr.fer.zemris.java.hw15.dao.DAO;
import hr.fer.zemris.java.hw15.dao.DAOException;
import hr.fer.zemris.java.hw15.model.BlogComment;
import hr.fer.zemris.java.hw15.model.BlogEntry;
import hr.fer.zemris.java.hw15.model.BlogUser;

import javax.persistence.Query;
import java.util.List;

/**
 * This class represents a DAO implementation using the JPA technology.
 *
 * @author Luka Čupić
 */
public class JPADAOImpl implements DAO {

	@Override
	public BlogEntry getEntry(Long id) throws DAOException {
		BlogEntry entry;
		try {
			entry = JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
			return entry;
		} catch (Exception ex) {
			throw new DAOException("An error has occurred while obtaining the blog entry.", ex);
		}
	}

	@Override
	public List<BlogEntry> getEntries(Long id) throws DAOException {
		try {
			BlogUser user = JPAEMProvider.getEntityManager().find(BlogUser.class, id);
			return (user != null) ? user.getEntries() : null;
		} catch (Exception ex) {
			throw new DAOException("An error has occurred while obtaining the list of entries.", ex);
		}
	}

	@Override
	public void addNewEntry(BlogEntry entry) throws DAOException {
		try {
			JPAEMProvider.getEntityManager().persist(entry);
		} catch (Exception ex) {
			throw new DAOException("An error has occurred while adding a new entry.", ex);
		}
	}

	@Override
	public void updateEntry(BlogEntry entry) throws DAOException {
		try {
			JPAEMProvider.getEntityManager().merge(entry);
		} catch (Exception ex) {
			throw new DAOException("An error has occurred while updating the entry.", ex);
		}
	}

	@Override
	public void addNewComment(BlogComment comment) throws DAOException {
		try {
			JPAEMProvider.getEntityManager().persist(comment);
		} catch (Exception ex) {
			throw new DAOException("An error has occurred while adding a new comment.", ex);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public BlogUser getUser(String nick) throws DAOException {
		try {
			String query = "SELECT DISTINCT u FROM BlogUser AS u WHERE u.nick=:nick";

			Query q = JPAEMProvider.getEntityManager().createQuery(query).setParameter("nick", nick);
			List<BlogUser> users = (List<BlogUser>) q.getResultList();

			return (users != null && users.size() != 0) ? users.get(0) : null;
		} catch (Exception ex) {
			throw new DAOException("An error has occurred while obtaining the user.", ex);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<BlogUser> getUsers() throws DAOException {

		try {
			String query = "SELECT DISTINCT u FROM BlogUser AS u";
			Query q = JPAEMProvider.getEntityManager().createQuery(query);

			return (List<BlogUser>) q.getResultList();
		} catch (Exception ex) {
			throw new DAOException("An error has occurred while obtaining the list of users.", ex);
		}
	}

	@Override
	public void addUser(BlogUser user) throws DAOException {
		try {
			JPAEMProvider.getEntityManager().persist(user);
		} catch (Exception ex) {
			throw new DAOException("An error has occurred while adding the user.", ex);
		}
	}
}