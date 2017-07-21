package hr.fer.zemris.java.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a servlet which is used to calculate
 * the sine and the cosine of a given set of values. The result
 * is delegated further to the "trigonometric.jsp" which displays
 * the result.
 *
 * @author Luka Čupić
 */
@WebServlet("/trigonometric")
public class TrigonometricServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html; charset=utf-8");
		resp.setStatus(HttpServletResponse.SC_OK);

		int a = 0;
		int b = 360;
		try {
			a = Integer.parseInt(req.getParameter("a"));
		} catch (NumberFormatException ignorable) {
		}
		try {
			b = Integer.parseInt(req.getParameter("b"));
		} catch (NumberFormatException ignorable) {
		}

		if (a > b) {
			int temp = a;
			a = b;
			b = temp;
		}

		if (b > a + 720) {
			b = a + 720;
		}

		List<Double[]> trigResults = new ArrayList<>();
		for (int i = a; i <= b; i++) {
			double deg = i * Math.PI / 180;
			trigResults.add(new Double[]{Double.valueOf(i), Math.sin(deg), Math.cos(deg)});
		}

		req.setAttribute("trigResults", trigResults);
		req.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp").forward(req, resp);
	}
}
