package hr.fer.zemris.java.hw15.web.sevlets;

import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.model.BlogEntry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * This class implements a web servlet which adds a new entry on to
 * the blog. The servlet expects two parameters from the request context:
 * "title" and "text" which represent title and text of the entry.
 * Additionally, the attribute "current.user.nick" must be present in the
 * session context, which represents the nickname of the user who added the
 * entry.
 *
 * @author Luka Čupić
 */
@WebServlet("/servleti/submitEntry")
public class NewEntryServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String nick = (String) req.getSession().getAttribute("current.user.nick");
		Date now = new Date();

		BlogEntry entry = new BlogEntry();
		entry.setTitle(req.getParameter("title"));
		entry.setText(req.getParameter("text"));
		entry.setCreator(DAOProvider.getDao().getUser(nick));
		entry.setCreatedAt(now);
		entry.setLastModifiedAt(now);

		DAOProvider.getDao().addNewEntry(entry);
		resp.sendRedirect(req.getContextPath() + "/servleti/author/" + nick);
	}
}