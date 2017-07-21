package hr.fer.zemris.hw18.servlets;

import com.google.gson.Gson;
import hr.fer.zemris.hw18.model.PicturesHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Fetches the image tags to be displayed on the web page.
 *
 * @author Luka Čupić
 */
@WebServlet("/servlets/tags")
public class TagsFetchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");

        PicturesHandler.contextPath = req.getServletContext().getRealPath("/WEB-INF/");
        String[] tags = PicturesHandler.getTags();

        Gson gson = new Gson();
        String jsonText = gson.toJson(tags);

        resp.getWriter().write(jsonText);
        resp.getWriter().flush();
    }
}