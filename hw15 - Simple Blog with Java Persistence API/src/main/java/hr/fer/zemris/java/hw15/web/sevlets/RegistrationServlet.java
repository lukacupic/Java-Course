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
 * This class implements a web servlet which performs a user registration.
 * The servlet expects the following parameters: "firstName" (the first name
 * of the user), "lastName" (the last name of the user), "email" (the user's
 * e-mail address), "nick" (the user's nickname) and "password" (the user's
 * password).
 *
 * @author Luka Čupić
 */
@WebServlet("/servleti/do_register")
public class RegistrationServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Util.resetErrors(req);

		String firstName = req.getParameter("firstName");
		String lastName = req.getParameter("lastName");
		String email = req.getParameter("email");
		String nick = req.getParameter("nick");
		String pass = req.getParameter("password");

		BlogUser user = DAOProvider.getDao().getUser(nick);

		if (user == null) {
			BlogUser newUser = new BlogUser();
			newUser.setFirstName(firstName);
			newUser.setLastName(lastName);
			newUser.setEmail(email);
			newUser.setNick(nick);
			newUser.setPasswordHash(Security.hashPassword(pass));

			DAOProvider.getDao().addUser(newUser);

			req.getRequestDispatcher("/WEB-INF/pages/registrationSuccess.jsp").forward(req, resp);
		} else {
			req.getSession().setAttribute("regError",
				"A user with the provided nickname already exists! Please chooose another one.");
			req.getRequestDispatcher("/WEB-INF/pages/registration.jsp").forward(req, resp);
		}
	}
}