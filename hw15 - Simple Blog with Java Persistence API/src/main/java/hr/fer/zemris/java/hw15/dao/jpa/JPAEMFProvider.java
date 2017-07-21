package hr.fer.zemris.java.hw15.dao.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * This class provides access to the "global" {@link EntityManagerFactory},
 * for the entire context of the web application. Using the EMF object, one
 * can obtain {@link EntityManager} objects which are then used for database
 * access (to perform queries, updates etc.).
 *
 * @author Luka Čupić
 */
public class JPAEMFProvider {

	/**
	 * Represents the EMF object.
	 */
	private static EntityManagerFactory emf;

	/**
	 * Gets an instance of the Entity Manager Factory object.
	 *
	 * @return the EMF object.
	 */
	public static EntityManagerFactory getEmf() {
		return emf;
	}

	/**
	 * Sets a new instance of the Entity Manager Factory object.
	 *
	 * @param emf the new EMF object
	 */
	public static void setEmf(EntityManagerFactory emf) {
		JPAEMFProvider.emf = emf;
	}
}