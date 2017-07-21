package hr.fer.zemris.java.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class represents a servlet which is used to select the
 * background color for the web pages. If no color is selected,
 * the default color will be {@code white}.
 *
 * @author Luka Čupić
 */
@WebServlet("/setcolor")
public class ColorSetterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);

        String color = req.getParameter("color");
        req.getSession().setAttribute("pickedBgCol", color);

        req.getRequestDispatcher("/WEB-INF/pages/colors.jsp").forward(req, resp);
    }
}