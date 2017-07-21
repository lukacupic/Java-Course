package hr.fer.zemris.java.hw15.web.sevlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class implements a web servlet which redirects the
 * registration request to the JSP "registration.jsp".
 *
 * @author Luka Čupić
 */
@WebServlet("/servleti/register")
public class RegistrationRedirect extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/pages/registration.jsp").forward(req, resp);
	}
}