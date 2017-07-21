package hr.fer.zemris.java.servlets.voting;

import hr.fer.zemris.java.servlets.voting.GlasanjeServlet.Band;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * This class represents a servlet which records a vote
 * given on the "glasanjeIndex.jsp" page. This vote is
 * then updated in the "glasanje-rezultati.txt" file;
 * this file, if non-existent, will be created, and all
 * of the votes will be set to zero.
 *
 * @author Luka Čupić
 */
@WebServlet("/glasanje-glasaj")
public class GlasanjeGlasajServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int id = Integer.parseInt(req.getParameter("id"));

		String filename = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		Path path = Paths.get(filename);

		String newContents = "";

		if (!Files.exists(path)) {
			Files.createFile(path);

			for (Band band : (List<Band>) req.getSession().getAttribute("bands")) {
				newContents += band.getID() + "\t" + (band.getID() == id ? 1 : 0) + "\n";
			}
		}

		List<String> lines = Files.readAllLines(path);
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);

			String[] parts = line.split("\t");
			try {
				int currentID = Integer.parseInt(parts[0]);
				if (currentID != id) continue;

				lines.set(i, id + "\t" + (Integer.parseInt(parts[1]) + 1));
				break;
			} catch (Exception ignorable) {
				req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
				return;
			}
		}

		if (newContents.isEmpty()) {
			for (String line : lines) {
				newContents += line + "\n";
			}
		}
		newContents = newContents.substring(0, newContents.length() - 1);

		Files.write(path, newContents.getBytes());
		resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
	}
}