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
 * This class implements a web servlet which does an update
 * on the specified entry. The servlet expects three parameters
 * from the request context: "entryID" (the ID of the entry to
 * modify), "title" (the new title of the entry) and "text" (
 * the nex text of the entry).
 *
 * @author Luka Čupić
 */
@WebServlet("/servleti/editEntry")
public class 	EditEntryServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long id = Long.valueOf(req.getParameter("entryID"));
		BlogEntry entry = DAOProvider.getDao().getEntry(id);

		entry.setTitle(req.getParameter("title"));
		entry.setText(req.getParameter("text"));

		DAOProvider.getDao().updateEntry(entry);

		String nick = (String) req.getSession().getAttribute("current.user.nick");
		resp.sendRedirect(req.getContextPath() + "/servleti/author/" + nick);
	}
}
