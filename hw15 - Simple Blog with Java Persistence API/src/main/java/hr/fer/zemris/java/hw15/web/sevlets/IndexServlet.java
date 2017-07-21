package hr.fer.zemris.java.hw15.web.sevlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class represents a servlet which redirects the "index.jsp"
 * to "servleti/main".
 *
 * @author Luka Čupić
 */
@WebServlet("/index.jsp")
public class IndexServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.sendRedirect(req.getContextPath() + "/servleti/main");
	}
}