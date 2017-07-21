package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * This class represents a server worker which is used
 * for drawing a simple circle on a PNG image of dimensions
 * 200x200 pixels.
 *
 * @author Luka Čupić
 */
public class CircleWorker implements IWebWorker {

    @Override
    public void processRequest(RequestContext context) throws Exception {
        BufferedImage bim = new BufferedImage(200, 200, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g2d = bim.createGraphics();

        context.setMimeType("image/png");

        Random rand = new Random();
        g2d.setColor(new Color(
            Math.abs(rand.nextInt()) % 256,
            Math.abs(rand.nextInt()) % 256,
            Math.abs(rand.nextInt()) % 256)
        );
        g2d.fillOval(0, 0, 200, 200);

        g2d.dispose();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bim, "png", bos);
            context.write(bos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Worker failed to do the job!");
        }
    }
}
