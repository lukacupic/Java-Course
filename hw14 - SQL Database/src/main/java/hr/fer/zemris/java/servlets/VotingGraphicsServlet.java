package hr.fer.zemris.java.servlets;

import hr.fer.zemris.java.model.PollOption;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * This class represents a servlet which dynamically creates
 * a pie chart whose data set represents the results of the
 * poll.
 * The servlet obtains a list of {@link PollOption} from the
 * servlet context and uses this data as the pie chart data
 * set.
 *
 * @author Luka Čupić
 */
@WebServlet("/servleti/glasanje-grafika")
public class VotingGraphicsServlet extends HttpServlet {

	@Override
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("image/png; charset=utf-8");
		resp.setStatus(HttpServletResponse.SC_OK);

		List<PollOption> options = (List<PollOption>) getServletContext().getAttribute("pollOptions");

		PieDataset dataset = createDataset(options);
		JFreeChart chart = createChart(dataset, "");

		BufferedImage image = chart.createBufferedImage(300, 300);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(image, "png", bos);
		resp.getOutputStream().write(bos.toByteArray());
	}

	/**
	 * Creates a new data set from the given data.
	 *
	 * @return a pie chart data set object representing the data set
	 */
	private PieDataset createDataset(List<PollOption> options) {
		DefaultPieDataset result = new DefaultPieDataset();
		for (PollOption opt : options) {
			result.setValue(opt.getOptionTitle(), opt.getVotesCount());
		}
		return result;
	}

	/**
	 * Creates a new chart object from the given data set.
	 *
	 * @param dataset the data set
	 * @param title   the pie chart title
	 * @return a new chart object from the given data set
	 */
	private JFreeChart createChart(PieDataset dataset, String title) {
		JFreeChart chart = ChartFactory.createPieChart3D(
			title, dataset, true, true, false
		);
		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		return chart;
	}
}
