package hr.fer.zemris.java.servlets;

import hr.fer.zemris.java.dao.DAOProvider;
import hr.fer.zemris.java.model.Poll;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class implements a servlet which displays the
 * index page of this web application.
 *
 * @author Luka Čupić
 */
@WebServlet("/servleti/index.html")
public class VotingIndexServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html; charset=utf-8");
		resp.setStatus(HttpServletResponse.SC_OK);

		StringBuilder sb = new StringBuilder();
		sb.append("<html>\n");
		sb.append("<body>\n");

		sb.append("<h2>Please, select your poll:</h2>");

		sb.append("<ol>");
		for (Poll poll : DAOProvider.getDao().getPolls()) {
			String url = req.getContextPath() + "/servleti/glasanje?pollID=" + poll.getID();
			sb.append("<li><a href=").append(url).append(">");
			sb.append(poll.getTitle()).append("</a>").append("</li>");
		}
		sb.append("</ol>");

		sb.append("</body>\n");
		sb.append("</html>\n");

		resp.getWriter().write(sb.toString());
	}
}