package hr.fer.zemris.java.servlets;

import hr.fer.zemris.java.dao.DAOProvider;
import hr.fer.zemris.java.model.PollOption;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class implements a servlet which displays the poll voting
 * results onto the HTML page.
 *
 * @author Luka Čupić
 */
@WebServlet("/servleti/glasanje-rezultati")
public class VotingResultsServlet extends HttpServlet {

	/**
	 * Represents all options of this poll.
	 */
	private List<PollOption> currentOptions;

	/**
	 * Represents the maximum number of votes of the poll.
	 */
	private int maxVotes;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html; charset=utf-8");
		resp.setStatus(HttpServletResponse.SC_OK);

		int pollID = (int) req.getServletContext().getAttribute("pollID");

		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append("<body>");

		displayTable(sb, pollID);
		displayPieChart(sb, req);
		displayXLS(sb, req);
		displayWinners(sb);

		sb.append("</body>");
		sb.append("</html>");

		resp.getWriter().write(sb.toString());
	}

	/**
	 * Creates an HTML table representing the poll results data and
	 * appends it to the provided string builder. It also sorts the
	 *
	 * @param sb     the string builder
	 * @param pollID the ID of the poll
	 */
	private void displayTable(StringBuilder sb, int pollID) {
		sb.append("<h1>Rezultati glasanja</h1>");
		sb.append("<p>Ovo su rezultati glasanja:<p>");

		sb.append("<table border=1>");

		List<PollOption> currentOptions = new ArrayList<>();

		List<PollOption> allOptions = DAOProvider.getDao().getPollOptions().stream()
			.filter(pollOption -> pollOption.getPollID() == pollID)
			.sorted(Comparator.reverseOrder())
			.collect(Collectors.toList());

		for (PollOption opt : allOptions) {
			sb.append("<tr><td>").append(opt.getOptionTitle()).append("</td>");
			sb.append("<td>").append(opt.getVotesCount()).append("</td></tr>");

			currentOptions.add(opt);
			if (opt.getVotesCount() > maxVotes) {
				maxVotes = opt.getVotesCount();
			}
		}
		this.currentOptions = currentOptions;
		getServletContext().setAttribute("pollOptions", currentOptions);

		sb.append("</table>");
	}

	/**
	 * Creates a pie chart image representing the poll results data
	 * and appends the HTML code which displays it to the provided
	 * string builder.
	 *
	 * @param sb  the string builder
	 * @param req the request context
	 */
	private void displayPieChart(StringBuilder sb, HttpServletRequest req) {
		sb.append("<h2>Grafički prikaz rezultata</h2>");
		String url = req.getContextPath() + "/servleti/glasanje-grafika";
		sb.append("<img alt=\"Pie-chart\" src=\"").append(url).append("\" width=\"400\" height=\"400\"/>");
	}

	/**
	 * Creates an XML file representing the poll results data and
	 * appends the HTML code which displays it to the provided
	 * string builder.
	 *
	 * @param sb  the string builder
	 * @param req the request context
	 */
	private void displayXLS(StringBuilder sb, HttpServletRequest req) {
		sb.append("<h2>Rezultati u XLS formatu</h2>");
		String url = req.getContextPath() + "/servleti/glasanje-xls";
		sb.append("Rezultati u XLS formatu dostupni su <a href=").append(url).append(">ovdje</a></p>");
	}

	/**
	 * Creates an HTML code which displays the winners of the
	 * poll and appends it to the provided string builder.
	 *
	 * @param sb the string builder
	 */
	private void displayWinners(StringBuilder sb) {
		List<PollOption> winners = getWinners(currentOptions);

		sb.append("<h2>Razno</h2>");
		sb.append("<p>Pobjednici:");

		sb.append("<ul>");
		for (PollOption w : winners) {

			String link = "<a href=" + w.getOptionLink() + ">" + w.getOptionTitle() + "</a";
			sb.append("<li>").append(link).append("</li>");
		}
		sb.append("</ul>");
	}

	/**
	 * Obtains the winners of the poll; the winners are the ones with
	 * the most votes.
	 *
	 * @param currentOptions the options of the current poll
	 * @return a list of poll winners
	 */
	private List<PollOption> getWinners(List<PollOption> currentOptions) {
		int max = maxVotes;
		return currentOptions.stream()
			.filter(pollOption -> pollOption.getVotesCount() == max)
			.collect(Collectors.toList());
	}
}