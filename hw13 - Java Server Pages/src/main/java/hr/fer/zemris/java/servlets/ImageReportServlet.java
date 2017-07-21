package hr.fer.zemris.java.servlets;

import javafx.util.Pair;
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
import java.util.Enumeration;
import java.util.List;

/**
 * This class represents a servlet which dynamically creates
 * a pie chart whose data set represents the usages of three
 * different operating systems.
 * The servlet accepts a set of parameters, representing the
 * name-value pairs, which will be shown in the pie chart.
 * If no parameters are provided, the default parameters for
 * Operating Systems will be used.
 *
 * @author Luka Čupić
 */
@WebServlet("/reportImage")
public class ImageReportServlet extends HttpServlet {

	List<Pair<String, Integer>> dataset;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("image/png; charset=utf-8");
		resp.setStatus(HttpServletResponse.SC_OK);

		PieDataset dataset = createDataset(req);
		JFreeChart chart = createChart(dataset, "");

		BufferedImage image = chart.createBufferedImage(300, 300);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(image, "png", bos);
		resp.getOutputStream().write(bos.toByteArray());
	}

	/**
	 * Creates a new data set from arbitrary chosen data.
	 *
	 * @return a pie chart data set object representing the data set
	 */
	private PieDataset createDataset(HttpServletRequest req) {
		DefaultPieDataset result = new DefaultPieDataset();

		Enumeration<String> names = req.getParameterNames();
		if (names.hasMoreElements()) {
			while (names.hasMoreElements()) {
				String name = names.nextElement();
				result.setValue(name, Integer.parseInt(req.getParameter(name)));
			}
		} else {
			result.setValue("Linux", 42);
			result.setValue("Mac", 13);
			result.setValue("Windows", 56);
		}

		return result;
	}

	/**
	 * Creates a new chart object from the given data set.
	 *
	 * @param dataset the dataset
	 * @param title   the pie chart title
	 * @return a new chart object from the given data set
	 */
	private JFreeChart createChart(PieDataset dataset, String title) {
		JFreeChart chart = ChartFactory.createPieChart3D(title, dataset, true, true, false);
		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		return chart;
	}
}
