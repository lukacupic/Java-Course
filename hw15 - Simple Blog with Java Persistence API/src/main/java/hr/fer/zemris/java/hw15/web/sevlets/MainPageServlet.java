package hr.fer.zemris.java.hw15.web.sevlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class implements a web servlet which redirects a call
 * to the JSP which provides the context of the home page.
 *
 * @author Luka Čupić
 */
@WebServlet("/servleti/main")
public class MainPageServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, resp);
	}
}