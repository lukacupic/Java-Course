package hr.fer.zemris.hw18.servlets;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Fetches an image from the disk, located in the "WEB-INF/slike"
 * folder.
 *
 * @author Luka Čupić
 */
@WebServlet("/servlets/get-picture")
public class PictureFetchServlet extends HttpServlet {

    /**
     * A relative path to the images folder.
     */
    private static final String photosFolder = "WEB-INF/slike/";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String filename = req.getParameter("name");
        String realPhoto = req.getServletContext().getRealPath(Paths.get(photosFolder).resolve(Paths.get(filename)).toString());

        BufferedImage picture = loadImage(realPhoto);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(picture, "jpg", bos);
        resp.getOutputStream().write(bos.toByteArray());
    }

    /**
     * Loads the image specified by the given path.
     *
     * @param path the path of the image
     * @return the image specified by the given path
     * @throws IOException if an I/O error occurs
     */
    private BufferedImage loadImage(String path) throws IOException {
        return ImageIO.read(Paths.get(path).toUri().toURL());
    }
}
