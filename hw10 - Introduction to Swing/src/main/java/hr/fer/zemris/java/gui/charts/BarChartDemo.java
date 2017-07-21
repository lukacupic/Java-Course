package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a JFrame object which, on it's surface,
 * draws an instance of the {@link BarChartComponent} which holds
 * the data read from a file which is represented by a {@link BarChart}
 * object.
 *
 * @author Lula Čupić
 */
public class BarChartDemo extends JFrame {

    /**
     * Represents the chart object drawn by this object.
     */
    private BarChart chart;

    /**
     * Represents the path to the file containing the chart data.
     */
    private Path path;

    /**
     * The constructor.
     *
     * @param chart the chart to draw
     */
    public BarChartDemo(BarChart chart, Path path) {
        this.chart = chart;
        this.path = path;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(520, 350);
        setVisible(true);
        setTitle("BarChart Demo");

        initGUI();
    }

    /**
     * A helper method for initializing the GUI.
     */
    private void initGUI() {
        setLayout(new BorderLayout());

        add(new BarChartComponent(chart), BorderLayout.CENTER);

        JLabel label = new JLabel(path.toAbsolutePath().toString());
        label.setHorizontalAlignment(JLabel.CENTER);
        add(label, BorderLayout.PAGE_START);
    }

    /**
     * The main method.
     *
     * @param args a single-element array, representing
     *             the path to the file with data
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please, provide an argument.");
            System.exit(1);
        }

        List<String> lines = null;
        try {
            lines = readLines(args[0]);
        } catch (IOException e) {
            System.out.println("Error reading file!");
            System.exit(1);
        }

        BarChart chart = parseData(lines.toArray(new String[0]));
        SwingUtilities.invokeLater(() -> new BarChartDemo(chart, Paths.get(args[0])));
    }

    /**
     * Parses the data extracted from the given lines and converts
     * it into a BarChart object.
     *
     * @param lines the lines which contain the data
     * @return a BarChart representation of the data
     */
    private static BarChart parseData(String[] lines) {
        String yLabel = lines[0]; // line 1
        String xLabel = lines[1]; // line 2

        // line 3
        List<XYValue> pairs = new ArrayList<>();
        for (String s : lines[2].split(" ")) {
            String[] values = s.split(",");

            int x = Integer.parseInt(values[0]);
            int y = Integer.parseInt(values[1]);
            pairs.add(new XYValue(x, y));
        }

        int minY = Integer.parseInt(lines[3]); // line 4
        int maxY = Integer.parseInt(lines[4]); // line 5
        int dist = Integer.parseInt(lines[5]); // line 6

        return new BarChart(pairs, xLabel, yLabel, minY, maxY, dist);
    }

    /**
     * Reads all lines from the specified file.
     *
     * @param path the string representation of the file to read
     * @return a list of all the lines read in the given file
     * @throws IOException if the file cannot be found
     */
    private static List<String> readLines(String path) throws IOException {
        return Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
    }
}
