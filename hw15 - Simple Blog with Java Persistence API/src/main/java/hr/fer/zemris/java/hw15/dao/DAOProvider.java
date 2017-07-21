package hr.fer.zemris.java.hw15.dao;

import hr.fer.zemris.java.hw15.dao.jpa.JPADAOImpl;

/**
 * This is a singleton class capable of providing a service provider
 * towards the data persistence subsystem.
 *
 * @author Luka Čupić
 */
public class DAOProvider {

	/**
	 * The DAO object.
	 */
	private static DAO dao = new JPADAOImpl();

	/**
	 * Returns the DAO object.
	 *
	 * @return object encapsulating the access towards the data
	 * persistence layer
	 */
	public static DAO getDao() {
		return dao;
	}
}