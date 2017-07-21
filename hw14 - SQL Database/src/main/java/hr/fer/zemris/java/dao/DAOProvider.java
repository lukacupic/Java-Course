package hr.fer.zemris.java.dao;

import hr.fer.zemris.java.dao.sql.SQLDAO;

/**
 * This is a singleton class which returns a service provider
 * towards the data persistence subsystem.
 */
public class DAOProvider {

	/**
	 * The DAO object.
	 */
	private static DAO dao = new SQLDAO();

	/**
	 * Returns the DAO object.
	 *
	 * @return object encapsulating the access for the data
	 * persistence layer
	 */
	public static DAO getDao() {
		return dao;
	}
}