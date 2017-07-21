package hr.fer.zemris.java.hw15.web.sevlets;

import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.model.BlogUser;
import hr.fer.zemris.java.hw15.web.Security;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class implements a web servlet which performs a login
 * of the user. The user's nickname and password hash are checked
 * against the database for validation.
 *
 * @author Luka Čupić
 */
@WebServlet("/servleti/do_login")
public class LoginServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Util.resetErrors(req);

		String nick = req.getParameter("nick");
		req.getSession().setAttribute("current.user.nick", nick);

		String pass = req.getParameter("password");

		BlogUser user = DAOProvider.getDao().getUser(nick);

		if (user == null) {
			req.getSession().setAttribute("nickError", "User with nickname " + nick + " does not exist!");
			req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, resp);
			return;
		}

		if (user.getPasswordHash().equals(Security.hashPassword(pass))) {
			req.getSession().setAttribute("current.user.id", user.getId());
			req.getSession().setAttribute("current.user.fn", user.getFirstName());
			req.getSession().setAttribute("current.user.ln", user.getLastName());
		} else {
			req.getSession().setAttribute("matchError", "Invalid password!");
		}
		req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, resp);
	}
}