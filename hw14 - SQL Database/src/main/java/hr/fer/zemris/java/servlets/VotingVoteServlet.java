package hr.fer.zemris.java.servlets;

import hr.fer.zemris.java.dao.DAOProvider;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class represents a servlet which records a vote,
 * updates the database and redirects to the results page.
 *
 * @author Luka Čupić
 */
@WebServlet("/servleti/glasanje-glasaj")
public class VotingVoteServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html; charset=utf-8");
		resp.setStatus(HttpServletResponse.SC_OK);

		int id = Integer.parseInt(req.getParameter("id"));
		DAOProvider.getDao().incrementPollOptionsVote(id);

		int pollID = DAOProvider.getDao().getPollOption(id).getPollID();
		req.getServletContext().setAttribute("pollID", pollID);

		resp.sendRedirect(req.getContextPath() + "/servleti/glasanje-rezultati");
	}
}