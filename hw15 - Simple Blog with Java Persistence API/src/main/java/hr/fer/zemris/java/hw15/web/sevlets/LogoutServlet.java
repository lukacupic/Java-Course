package hr.fer.zemris.java.hw15.web.sevlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class implements a web servlet which performs a logout
 * of the currently logged in user. All data about the current
 * user will be removed from the session context.
 *
 * @author Luka Čupić
 */
@WebServlet("/servleti/logout")
public class LogoutServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getSession().invalidate();
		req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, resp);
	}
}
