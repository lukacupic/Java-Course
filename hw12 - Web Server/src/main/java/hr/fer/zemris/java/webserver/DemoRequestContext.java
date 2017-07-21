package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is a demonstration program of the {@link RequestContext}
 * class.
 *
 * @author Luka Čupić
 */
public class DemoRequestContext {

    /**
     * The main method.
     *
     * @param args command line arguments; not used in this class
     */
    public static void main(String[] args) {
        try {
            demo1("primjer1.txt", "ISO-8859-2");
            demo1("primjer2.txt", "UTF-8");
            demo2("primjer3.txt", "UTF-8");
        } catch (IOException ex) {
            System.out.println("An error has occurred while performing" +
                "the demonstration!"
            );
        }
    }

    /**
     * The method for the first type of the demonstration.
     *
     * @param filePath the path to the file
     * @param encoding the encoding to use
     * @throws IOException if an I/O error occurs
     */
    private static void demo1(String filePath, String encoding) throws IOException {
        OutputStream os = Files.newOutputStream(Paths.get(filePath));
        RequestContext rc = new RequestContext(os, new HashMap<>(), new HashMap<>(), new ArrayList<>());
        rc.setEncoding(encoding);
        rc.setMimeType("text/plain");
        rc.setStatusCode(205);
        rc.setStatusText("Idemo dalje");

        // Only at this point will header be created and written...
        rc.write("Čevapčići i Šiščevapčići.");
        os.close();
    }

    /**
     * The method for the second type of the demonstration.
     *
     * @param filePath the path to the file
     * @param encoding the encoding to use
     * @throws IOException if an I/O error occurs
     */
    private static void demo2(String filePath, String encoding) throws IOException {
        OutputStream os = Files.newOutputStream(Paths.get(filePath));
        RequestContext rc = new RequestContext(os, new HashMap<>(),
            new HashMap<>(),
            new ArrayList<>());
        rc.setEncoding(encoding);
        rc.setMimeType("text/plain");
        rc.setStatusCode(205);
        rc.setStatusText("Idemo dalje");
        rc.addRCCookie(new RequestContext.RCCookie("korisnik", "perica", 3600, "127.0.0.1", "/", false));
        rc.addRCCookie(new RequestContext.RCCookie("zgrada", "B4", null, null, "/", false));

        // Only at this point will header be created and written...
        rc.write("Čevapčići i Šiščevapčići.");
        os.close();
    }
}