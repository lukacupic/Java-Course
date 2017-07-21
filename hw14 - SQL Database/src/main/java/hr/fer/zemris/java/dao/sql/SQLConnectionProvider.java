package hr.fer.zemris.java.dao.sql;

import java.sql.Connection;

/**
 * This class provides a connection to the database. The connection
 * itself is given to each of the threads, meaning that each thread
 * has it's own connection. All of these connections are managed with
 * the ThreadLocal object.
 *
 * @author Luka Čupić
 */
public class SQLConnectionProvider {

	/**
	 * The object used for managing connection.
	 */
	private static ThreadLocal<Connection> connections = new ThreadLocal<>();

	/**
	 * Sets the connection for the current thread (or deletes the
	 * entry from the map if argument is {@code null}.
	 *
	 * @param con the connection to the database
	 */
	public static void setConnection(Connection con) {
		if (con == null) {
			connections.remove();
		} else {
			connections.set(con);
		}
	}

	/**
	 * Gets the connection available to the current thread (caller).
	 *
	 * @return the connection to the database
	 */
	public static Connection getConnection() {
		return connections.get();
	}
}