package hr.fer.zemris.java;

import hr.fer.zemris.java.dao.sql.SQLConnectionProvider;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * This class represents a filter which is started whenever a new
 * user(read: thread) is accessing the web application. All necessary
 * settings are initialized, among which is mapping the thread to a
 * connection, so that the {@link SQLConnectionProvider} is able to
 * provide a connection to the thread, later during the thread's
 * execution.
 *
 * @author Luka Čupić
 */
@WebFilter(filterName = "f1", urlPatterns = {"/servleti/*"})
public class ConnectionSetterFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
						 FilterChain chain) throws IOException, ServletException {

		DataSource ds = (DataSource) request.getServletContext().getAttribute("hr.fer.zemris.dbpool");
		Connection con;
		try {
			con = ds.getConnection();
		} catch (SQLException e) {
			throw new IOException("Database not available.", e);
		}
		SQLConnectionProvider.setConnection(con);
		try {
			chain.doFilter(request, response);
		} finally {
			SQLConnectionProvider.setConnection(null);
			try {
				con.close();
			} catch (SQLException ignorable) {
			}
		}
	}
}