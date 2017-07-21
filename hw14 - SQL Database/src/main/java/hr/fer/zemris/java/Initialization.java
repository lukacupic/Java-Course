package hr.fer.zemris.java;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import hr.fer.zemris.java.util.SQLCommands;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * This class implements a listener which initializes the database.
 * <p>
 * If either the database, the relations or the data is not present,
 * they will be created using default values.
 *
 * @author Luka Čupić
 */
@WebListener
public class Initialization implements ServletContextListener {

	/**
	 * A flag which tells whether the polls should be created.
	 */
	private boolean createPolls = false;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String[] values = null;
		try {
			values = readConfigFile(sce);
		} catch (IOException e) {
			System.exit(1);
		}

		String connectionURL = String.format("jdbc:derby://%s:%s/%s;user=%s;password=%s",
			values[0], values[1], values[2], values[3], values[4]
		);

		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass("org.apache.derby.jdbc.ClientDriver");
		} catch (PropertyVetoException ex) {
			throw new RuntimeException("An error has occurred while initializing the pool.", ex);
		}
		cpds.setJdbcUrl(connectionURL);

		sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);

		Connection con = null;
		PreparedStatement pst = null;
		try {
			Class.forName("org.apache.derby.jdbc.ClientDriver");
			con = cpds.getConnection();

			checkTables(con);

			pst = con.prepareStatement(SQLCommands.COUNT_POLLS);

			ResultSet rs = pst.executeQuery();
			rs.next();

			if (rs.getInt(1) == 0 || createPolls) {
				initTables(con);
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				pst.close();
			} catch (Exception ignorable) {
			}

			try {
				con.close();
			} catch (Exception ignorable) {
			}
		}
	}

	/**
	 * Initializes tables Polls and PollOptions with default values.
	 *
	 * @param con a connection to the database
	 * @throws SQLException if an error occurs while accessing the database
	 */
	private void initTables(Connection con) throws SQLException {
		initPoll(SQLCommands.CREATE_BANDS_POLL, SQLCommands.ADD_BANDS, con);
		initPoll(SQLCommands.CREATE_PROGRAMMING_POLL, SQLCommands.ADD_LANGUAGES, con);
	}

	private void initPoll(String createPoll, String[] addOptions, Connection con) throws SQLException {
		Statement st = con.createStatement();
		st.executeUpdate(createPoll, Statement.RETURN_GENERATED_KEYS);

		ResultSet rs = st.getGeneratedKeys();
		rs.next();
		int pollID = rs.getInt(1);

		for (int i = 0; i < addOptions.length; i++) {
			PreparedStatement pst = con.prepareStatement(addOptions[i]);
			pst.setInt(1, pollID);
			pst.execute();

			try {
				pst.close();
			} catch (SQLException ignorable) {
			}
		}
	}

	/**
	 * Checks if tables Polls and PollOptions exist, and if not, creates
	 * them.
	 *
	 * @param con a connection to the database
	 * @throws SQLException if an error occurs while accessing the database
	 */
	private void checkTables(Connection con) throws SQLException {
		try {
			PreparedStatement pst = con.prepareStatement(SQLCommands.CREATE_POLLS);
			pst.execute();

			try {
				pst.close();
			} catch (SQLException ignorable) {
			}

			pst = con.prepareStatement(SQLCommands.CREATE_POLL_OPTIONS);
			pst.execute();

			try {
				pst.close();
			} catch (SQLException ignorable) {
			}

			createPolls = true;
		} catch (SQLException ex) {
			if (!"X0Y32".equals(ex.getSQLState())) {
				throw ex;
			}
		}
	}

	/**
	 * Reads the "dbsettings.properties" configuration file.
	 *
	 * @param sce the context event
	 * @return an array of values, representing the properties' values
	 * @throws IOException if an I/O error occurs or if the configuration
	 *                     file is invalid
	 */
	private String[] readConfigFile(ServletContextEvent sce) throws IOException {
		String filename = sce.getServletContext().getRealPath(
			"/WEB-INF/dbsettings.properties"
		);

		FileInputStream is = new FileInputStream(filename);

		Properties p = new Properties();
		p.load(is);
		is.close();

		String[] keys = {"host", "port", "name", "user", "password"};
		String[] values = new String[keys.length];
		for (int i = 0; i < keys.length; i++) {
			if (!p.containsKey(keys[i])) {
				throw new IOException("Property '" + keys[i] + "' " + "is missing from the config file!");
			}
			values[i] = (String) p.get(keys[i]);
		}
		return values;
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}
}
