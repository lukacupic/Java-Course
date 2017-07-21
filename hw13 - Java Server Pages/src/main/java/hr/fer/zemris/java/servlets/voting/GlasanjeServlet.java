package hr.fer.zemris.java.servlets.voting;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a servlet which loads the
 * existing bands from the "glasanje-definicija.txt"
 * file which are then displayed on the "glasanjeIndex.jsp"
 * page.
 *
 * @author Luka Čupić
 */
@WebServlet("/glasanje")
public class GlasanjeServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html; charset=utf-8");
		resp.setStatus(HttpServletResponse.SC_OK);

		String filename = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
		List<String> lines = Files.readAllLines(Paths.get(filename));

		List<Band> bands = new ArrayList<>();
		try {
			for (String line : lines) {
				String[] parts = line.split("\t");
				bands.add(new Band(Integer.parseInt(parts[0]), parts[1], parts[2]));
			}
		} catch (Exception ex) {
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}

		req.getSession().setAttribute("bands", bands);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
	}

	/**
	 * This class represents a musical band. The class
	 * encapsulates the band's ID, it's name and their
	 * representative song.
	 *
	 * @author Luka Čupić
	 */
	public static class Band {

		/**
		 * The band's ID.
		 */
		private int id;

		/**
		 * The band's name.
		 */
		private String name;

		/**
		 * The band's representative song.
		 */
		private String song;

		/**
		 * Creates a new instance of this class.
		 *
		 * @param id   the band's ID
		 * @param name the band's name
		 * @param song the band's representative song
		 */
		public Band(int id, String name, String song) {
			this.id = id;
			this.name = name;
			this.song = song;
		}

		/**
		 * Gets the band's ID.
		 *
		 * @return the band's ID
		 */
		public int getID() {
			return id;
		}

		/**
		 * Gets the band's name.
		 *
		 * @return the band's name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Gets the band's representative song.
		 *
		 * @return the band's representative song
		 */
		public String getSong() {
			return song;
		}
	}
}