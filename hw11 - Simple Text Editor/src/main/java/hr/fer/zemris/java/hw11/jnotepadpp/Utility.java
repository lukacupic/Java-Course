package hr.fer.zemris.java.hw11.jnotepadpp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class represents a utility class for the {@link JNotepadPP}.
 * It offers static methods for performing some miscellaneous
 * tasks.
 *
 * @author Luka Čupić
 */
public class Utility {

    /**
     * Reads all bytes from the given input stream.
     *
     * @param input the input stream to read the bytes from
     * @return an array of bytes read from the given stream
     * @throws IOException if an I/O error occurs
     */
    public static byte[] readBytes(InputStream input) throws IOException {
        byte[] buffer = new byte[4096];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        return output.toByteArray();
    }
}
