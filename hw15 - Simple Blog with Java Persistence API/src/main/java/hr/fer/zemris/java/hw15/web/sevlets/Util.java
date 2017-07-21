package hr.fer.zemris.java.hw15.web.sevlets;

import javax.servlet.http.HttpServletRequest;

/**
 * This class provides utility method for some common operations
 * among the web servlets.
 *
 * @author Luka Čupić
 */
public class Util {

	/**
	 * Resets all potential errors caused by the wrong user input.
	 *
	 * @param req the request context
	 */
	public static void resetErrors(HttpServletRequest req) {
		req.getSession().setAttribute("regError", null);
		req.getSession().setAttribute("nickError", null);
		req.getSession().setAttribute("matchError", null);
	}
}