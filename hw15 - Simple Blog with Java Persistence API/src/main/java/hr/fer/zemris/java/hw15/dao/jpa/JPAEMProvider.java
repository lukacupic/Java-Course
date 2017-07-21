package hr.fer.zemris.java.hw15.dao.jpa;

import hr.fer.zemris.java.hw15.dao.DAOException;

import javax.persistence.EntityManager;

/**
 * This class provides access to the {@link EntityManager} objects,
 * which can be used for accessing the database (e.g. to perform
 * queries, updates etc.).
 *
 * @author Luka Čupić
 */
public class JPAEMProvider {

	/**
	 * An object providing LocalData objects to the application threads.
	 */
	private static ThreadLocal<LocalData> locals = new ThreadLocal<>();

	/**
	 * Obtains a new entity manager.
	 *
	 * @return a {@link EntityManager} object, used to access the database
	 */
	public static EntityManager getEntityManager() {
		LocalData ldata = locals.get();
		if (ldata == null) {
			ldata = new LocalData();
			ldata.em = JPAEMFProvider.getEmf().createEntityManager();
			ldata.em.getTransaction().begin();
			locals.set(ldata);
		}
		return ldata.em;
	}

	/**
	 * Closes the currently active entity manager.
	 *
	 * @throws DAOException if an error occurs while accessing the data
	 */
	public static void close() throws DAOException {
		LocalData ldata = locals.get();
		if (ldata == null) {
			return;
		}
		DAOException dex = null;
		try {
			ldata.em.getTransaction().commit();
		} catch (Exception ex) {
			dex = new DAOException("Unable to commit transaction.", ex);
		}
		try {
			ldata.em.close();
		} catch (Exception ex) {
			if (dex != null) {
				dex = new DAOException("Unable to close entity manager.", ex);
			}
		}
		locals.remove();
		if (dex != null) throw dex;
	}

	/**
	 * This class provides access to the database by
	 * encapsulating an {@link EntityManager} object.
	 *
	 * @author Luka Čupić
	 */
	private static class LocalData {

		/**
		 * The entity manager object.
		 */
		EntityManager em;
	}
}