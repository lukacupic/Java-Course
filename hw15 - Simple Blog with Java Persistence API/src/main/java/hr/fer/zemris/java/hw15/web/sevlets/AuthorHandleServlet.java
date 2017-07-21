package hr.fer.zemris.java.hw15.web.sevlets;

import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.model.BlogEntry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class implements a web servlet which parses amy URL
 * which starts with "/servleti/author/" and checks whether
 * the remaining parts of the URL are legal. If the are, an
 * appropriate action will be triggered.
 *
 * @author Luka Čupić
 */
@WebServlet("/servleti/author/*")
public class AuthorHandleServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			parseURL(req, resp);
		} catch (Exception ex) {
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
		}
	}

	/**
	 * A helper method for parsing the provided URL. If any errors
	 * occur while parsing, the caller method is obliged to handle it.
	 *
	 * @param req the request context
	 */
	private void parseURL(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String pathInfo = req.getPathInfo();
		if (pathInfo == null) {
			throw new RuntimeException();
		}

		String[] parts = pathInfo.split("/");

		String providedNick = parts[1];
		Long providedID = DAOProvider.getDao().getUser(providedNick).getId();

		req.getSession().setAttribute("provided.user.nick", providedNick);
		req.getSession().setAttribute("provided.user.id", providedID);

		if (parts.length == 2) {
			req.getRequestDispatcher("/WEB-INF/pages/author.jsp").forward(req, resp);
			return;
		}

		String currentNick = (String) req.getSession().getAttribute("current.user.nick");

		if ("new".equals(parts[2])) {
			if (!currentNick.equals(providedNick)) throw new RuntimeException();
			req.getRequestDispatcher("/WEB-INF/pages/newEntry.jsp").forward(req, resp);
		} else if ("edit".equals(parts[2])) {
			if (!currentNick.equals(providedNick)) throw new RuntimeException();
			req.getRequestDispatcher("/WEB-INF/pages/editEntry.jsp").forward(req, resp);
		} else {
			try {
				Long id = Long.valueOf(parts[2]);
				BlogEntry entry = DAOProvider.getDao().getEntry(id);

				if (entry == null || !providedNick.equals(entry.getCreator().getNick())) {
					throw new RuntimeException();
				}

				req.getSession().setAttribute("current.entry", entry);
				req.getRequestDispatcher("/WEB-INF/pages/entry.jsp").forward(req, resp);
			} catch (NumberFormatException ex) {
				throw new RuntimeException();
			}
		}
	}
}