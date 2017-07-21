package hr.fer.zemris.java.servlets.voting;

import hr.fer.zemris.java.servlets.voting.GlasanjeRezultatiServlet.BandScore;
import hr.fer.zemris.java.servlets.voting.GlasanjeServlet.Band;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * This class represents a servlet which displays
 * the voting results on a pie chart.
 *
 * @author Luka Čupić
 */
@WebServlet("/glasanje-grafika")
public class GlasanjeGrafikaServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<BandScore> scores = (List<BandScore>) req.getSession().getAttribute("scores");
		List<Band> bands = (List<Band>) req.getSession().getAttribute("bands");

		String chartURL = "/reportImage?";
		for (BandScore score : scores) {
			chartURL += bands.get(score.getID() - 1).getName();
			chartURL += "=";
			chartURL += score.getVotes();
			chartURL += "&";
		}
		chartURL = chartURL.substring(0, chartURL.length() - 1);

		resp.sendRedirect(req.getContextPath() + chartURL);
	}
}