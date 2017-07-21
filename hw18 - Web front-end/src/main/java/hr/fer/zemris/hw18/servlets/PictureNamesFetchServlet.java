package hr.fer.zemris.hw18.servlets;

import com.google.gson.Gson;
import hr.fer.zemris.hw18.model.Picture;
import hr.fer.zemris.hw18.model.PicturesHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Fetches a picture located on the disk to be displayed on
 * the web page.
 *
 * @author Luka Čupić
 */
@WebServlet("/servlets/thumbs")
public class PictureNamesFetchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tag = req.getParameter("tag");
        Picture[] pics = PicturesHandler.filterPictures(tag);

        Gson gson = new Gson();
        String jsonText = gson.toJson(pics);

        resp.getWriter().write(jsonText);
        resp.getWriter().flush();
    }
}
