package hr.fer.zemris.hw18.servlets;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Fetches an image thumbnail from the disk. If the thumbnail doesn't exist,
 * the original image will be loaded, resized, saved as the thumbnail and then
 * returned.
 *
 * @author Luka Čupić
 */
@WebServlet("/servlets/get-thumb")
public class ThumbFetchServlet extends HttpServlet {

    /**
     * A relative path to the images folder.
     */
    private static final String photosFolder = "WEB-INF/slike/";

    /**
     * A relative path to the thumbnails folder.
     */
    private static final String thumbsFolder = "WEB-INF/thumbnails/";

    /**
     * The width of the thumbnail.
     */
    private static final int SCALED_WIDTH = 150;

    /**
     * The height of the thumbnail.
     */
    private static final int SCALED_HEIGHT = 150;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String filename = req.getParameter("name");

        String realThumbsFolder = req.getServletContext().getRealPath(thumbsFolder);
        File thumbs = new File(realThumbsFolder);
        if (!thumbs.exists()) {
            thumbs.mkdir();
        }

        String realPhoto = req.getServletContext().getRealPath(Paths.get(photosFolder).resolve(Paths.get(filename)).toString());
        String realThumb = req.getServletContext().getRealPath(Paths.get(thumbsFolder).resolve(Paths.get(filename)).toString());


        BufferedImage thumb = null;
        try {
            thumb = loadImage(realThumb);
        } catch (Exception ignorable) {
        }

        if (thumb == null) {
            BufferedImage photo = loadImage(realPhoto);
            thumb = resize(photo);
            saveImage(thumb, realThumb);
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(thumb, "jpg", bos);
        resp.getOutputStream().write(bos.toByteArray());
    }

    /**
     * Returns a resized version of the given image.
     *
     * @param originalImage the image to resize
     * @return a resized version of the given image.
     * @throws IOException if an I/O error occurs
     */
    private BufferedImage resize(Image originalImage) throws IOException {
        BufferedImage scaledImage = new BufferedImage(SCALED_WIDTH, SCALED_HEIGHT, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = scaledImage.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.drawImage(originalImage, 0, 0, SCALED_WIDTH, SCALED_HEIGHT, null);
        g.dispose();

        return scaledImage;
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

    /**
     * Saves the image on the given path.
     *
     * @param image the image to save
     * @param path  the path
     * @throws IOException if an I/O error occurs
     */
    private void saveImage(BufferedImage image, String path) throws IOException {
        ImageIO.write(image, "jpg", new File(path));
    }
}
