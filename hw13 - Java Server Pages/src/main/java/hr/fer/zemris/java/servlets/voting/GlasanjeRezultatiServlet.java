package hr.fer.zemris.java.servlets.voting;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static hr.fer.zemris.java.servlets.voting.GlasanjeServlet.Band;

/**
 * This class represents a servlet which reads the existing
 * results from the "glasanje-rezultati.txt" file and forwards
 * the results to the "glasanjeRez.jsp" which then displays the
 * results.
 *
 * @author Luka Čupić
 */
@WebServlet("/glasanje-rezultati")
public class GlasanjeRezultatiServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String filename = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		Path path = Paths.get(filename);

		List<Band> bands = (List<Band>) req.getSession().getAttribute("bands");
		List<BandScore> scores = new ArrayList<>();

		if (!Files.exists(path)) {
			Files.createFile(path);

			for (Band band : bands) {
				scores.add(new BandScore(band.getID(), 0));
			}
		}

		List<String> lines = Files.readAllLines(path);
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);

			String[] parts = line.split("\t");
			try {
				scores.add(new BandScore(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
			} catch (Exception ignorable) {
				req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
				return;
			}
		}

		getWinners(scores, bands, req);

		req.getSession().setAttribute("scores", scores);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
	}

	/**
	 * Collects the voting winners and creates a new attribute
	 * in the current session, storing a list of winners.
	 *
	 * @param scores the list of scores
	 * @param bands  the list of bands
	 * @param req    the servlet request object
	 */
	private void getWinners(List<BandScore> scores, List<Band> bands, HttpServletRequest req) {
		int max = 0;
		for (BandScore score : scores) {
			if (score.getVotes() > max) {
				max = score.getVotes();
			}
		}

		List<Band> winners = new ArrayList<>();
		for (BandScore score : scores) {
			if (score.getVotes() == max) {
				winners.add(bands.get(score.getID() - 1));
			}
		}

		req.getSession().setAttribute("winners", winners);
	}

	/**
	 * This class represents the score received by each band.
	 * The class encapsulates a band's ID and the number of
	 * received votes.
	 *
	 * @author Luka Čupić
	 */
	public static class BandScore {

		/**
		 * The id of the band.
		 */
		private int id;

		/**
		 * The number of votes.
		 */
		private int votes;

		/**
		 * Creates a new instance of this class.
		 *
		 * @param id    the id of the band
		 * @param votes the number of votes
		 */
		public BandScore(int id, int votes) {
			this.id = id;
			this.votes = votes;
		}

		/**
		 * Gets the id of the band.
		 *
		 * @return the id of the band
		 */
		public int getID() {
			return id;
		}

		/**
		 * Gets the number of votes.
		 *
		 * @return the number of votes
		 */
		public int getVotes() {
			return votes;
		}
	}
}