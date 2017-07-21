package hr.fer.zemris.java.servlets;

import hr.fer.zemris.java.dao.DAOProvider;
import hr.fer.zemris.java.model.Poll;
import hr.fer.zemris.java.model.PollOption;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class represents a servlet which displays a poll voting
 * web page - on this page, users can vote for an arbitrary  poll
 * option.
 *
 * @author Luka Čupić
 */
@WebServlet("/servleti/glasanje")
public class VotingServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html; charset=utf-8");
		resp.setStatus(HttpServletResponse.SC_OK);

		int pollID = Integer.parseInt(req.getParameter("pollID"));

		StringBuilder sb = new StringBuilder();
		sb.append("<html>\n");
		sb.append("<body>\n");

		Poll poll = DAOProvider.getDao().getPoll(pollID);
		sb.append("<h1>").append(poll.getTitle()).append("</h1>");
		sb.append("<p>").append(poll.getMessage()).append("</p>");

		sb.append("<ol>");
		for (PollOption opt : DAOProvider.getDao().getPollOptions()) {
			if (opt.getPollID() != pollID) continue;

			String url = req.getContextPath() + "/servleti/glasanje-glasaj?id=" + opt.getID();
			sb.append("<li><a href=").append(url).append(">");
			sb.append(opt.getOptionTitle()).append("</a>").append("</li>");
		}
		sb.append("</ol>");

		sb.append("</body>\n");
		sb.append("</html>\n");

		resp.getWriter().write(sb.toString());
	}
}
