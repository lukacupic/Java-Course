package hr.fer.zemris.hw18.model;

import hr.fer.zemris.hw18.util.Utility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Represents a class which offers static methods for handling
 * pictures.
 *
 * @author Luka Čupić
 */
public class PicturesHandler {

    /**
     * The actual path of the images root directory.
     */
    public static String contextPath;

    /**
     * A list of pictures.
     */
    private static List<Picture> pictures = new ArrayList<>();

    /**
     * Represents the set of tags for all images.
     */
    private static String[] tags;

    /**
     * Returns all pictures with the specified tag.
     *
     * @param tag the tag to filter the pictures by
     * @return all pictures with the specified tag
     */
    public static Picture[] filterPictures(String tag) {
        List<Picture> filtered = new ArrayList<>();
        for (Picture pic : pictures) {
            if (Arrays.asList(pic.getTags()).contains(tag)) {
                filtered.add(pic);
            }
        }
        return filtered.toArray(new Picture[0]);
    }

    /**
     * Gets a set of tags from all the pictures.
     *
     * @return a set of all tags
     * @throws IOException if an I/O error occurs
     */
    public static String[] getTags() throws IOException {
        if (pictures.isEmpty()) {
            readInfo();
        }

        if (tags == null) {
            readTags();
        }

        return tags;
    }

    /**
     * Reads the tags from the image description file.
     */
    private static void readTags() {
        Set<String> tagsSet = new TreeSet<>();
        for (Picture pic : pictures) {
            tagsSet.addAll(Arrays.asList(pic.getTags()));
        }
        tags = tagsSet.toArray(new String[0]);
    }

    /**
     * Reads the information from the image description file.
     *
     * @throws IOException if an I/O error occurs
     */
    private static void readInfo() throws IOException {
        Path path = Paths.get(contextPath).resolve("opisnik.txt");

        List<String> lines = Files.readAllLines(path);
        parse(lines.toArray(new String[0]));
        System.out.println();
    }

    /**
     * Parses the image description file and creates new Picture objects
     * which are then added to the list.
     *
     * @param lines the list of lines to parse
     */
    private static void parse(String[] lines) {
        for (int i = 0; i < lines.length; i += 3) {
            String name = lines[i];
            String desc = lines[i + 1];
            String[] tags = Utility.trimStringArray(lines[i + 2].split(","));
            pictures.add(new Picture(name, desc, tags));
        }
    }
}
