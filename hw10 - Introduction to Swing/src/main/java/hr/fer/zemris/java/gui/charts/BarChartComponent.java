package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;


/**
 * This class provides an implementation of a bar chart. The chart is
 * created from the {@link BarChart} object provided through the class
 * constructor. If any of the y-values are negative, they will not be
 * shown on the chart, for legal values for y start at zero.
 *
 * @author Luka Čupić
 */
public class BarChartComponent extends JComponent {

    /**
     * The BarChart represented by this component.
     */
    private BarChart chart;

    /**
     * Represents the distance from the left edge of the component
     * to the y-axis.
     */
    private double horOffsetLeft;

    /**
     * Represents the distance from the right edge of the component
     * to the end of the x-axis.
     */
    private double horOffsetRight;

    /**
     * Represents the distance from the bottom of the component
     * to the x-axis.
     */
    private double vertOffsetBottom;

    /**
     * Represents the distance from the top of the component
     * to the end of the y-axis.
     */
    private double vertOffsetTop;

    /**
     * Represents the small part of each line, which extends
     * past the coordinate axis.
     */
    private double snip;

    /**
     * Represents the origin of the coordinate system.
     */
    private Point origin;

    /**
     * Creates a new {@link BarChart} component.
     *
     * @param chart the chart to be drawn
     */
    BarChartComponent(BarChart chart) {
        this.chart = chart;

        this.horOffsetRight = 20;
        this.vertOffsetBottom = 40;
        this.vertOffsetTop = 20;
        this.snip = 5;
    }

    @Override
    protected void paintComponent(Graphics g) {
        calculate(g);

        drawAxis(g);
        drawHorizontalLines(g);
        drawBars(g);
        drawVerticalLines(g);
        drawText(g);
    }

    /**
     * A helper method for drawing the x and the y axis on the chart graph.
     *
     * @param g the graphics object to draw upon
     */
    private void drawAxis(Graphics g) {
        g.drawLine(origin.x, origin.y, (int) xEndpoint(), origin.y);
        g.drawLine(origin.x, origin.y, origin.x, (int) yEndpoint());
    }

    /**
     * A helper method for drawing the horizontal lines on the chart graph.
     *
     * @param g the graphics object to draw upon
     */
    private void drawHorizontalLines(Graphics g) {
        int i = 0;
        for (double y = origin.y; y >= vertOffsetTop; y -= yAxisLength() / chart.noOfYValues()) {
            g.drawLine((int) (origin.x - snip), (int) y, (int) xEndpoint(), (int) y);

            String currentValue = String.valueOf(chart.getMinY() + chart.getDistance() * (i++));
            g.drawString(currentValue,
                (int) (origin.x - snip - 5 - getStringWidth(currentValue, g)),
                (int) (y + g.getFontMetrics().getAscent() / 2)
            );
        }
    }

    /**
     * A helper method for drawing the bars on the chart graph.
     *
     * @param g the graphics object to draw upon
     */
    private void drawBars(Graphics g) {
        List<XYValue> values = chart.getValues();
        for (int j = 0; j < values.size(); j++) {
            XYValue val = values.get(j);
            double barHeight = (double) val.getY() / chart.getMaxY() * yAxisLength();

            g.setColor(Color.ORANGE);
            g.fillRect((int) (origin.x + j * barWidth()),
                (int) (origin.y - barHeight),
                (int) barWidth(),
                (int) barHeight
            );
            g.setColor(Color.BLACK);
        }
    }

    /**
     * A helper method for drawing the vertical lines on the chart graph.
     *
     * @param g the graphics object to draw upon
     */
    private void drawVerticalLines(Graphics g) {
        int i = 0;
        List<XYValue> values = chart.getValues();
        for (double x = origin.x; x <= getWidth() - horOffsetRight; x += barWidth()) {
            g.drawLine((int) x, (int) (origin.y + snip), (int) x, (int) yEndpoint());

            if (x == getWidth() - horOffsetRight || i >= values.size()) continue;

            int currentValue = values.get(i++).getX();
            g.drawString(String.valueOf(currentValue),
                (int) (x + (xAxisLength() / values.size()) / 2),
                origin.y + g.getFontMetrics().getAscent()
            );
        }
    }

    /**
     * A helper method for drawing the axis labels.
     *
     * @param g the graphics object to draw upon
     */
    private void drawText(Graphics g) {
        // draw the horizontal text
        g.drawString(chart.getXLabel(),
            (int) (origin.x + xAxisLength() / 2 - getStringWidth(chart.getXLabel(), g) / 2),
            getHeight() - 10);

        // flip the coordinate system
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform defaultAt = g2d.getTransform();

        AffineTransform at = new AffineTransform();
        at.rotate(-Math.PI / 2);
        g2d.setTransform(at);

        // draw the vertical text
        g.drawString(chart.getYLabel(),
            (int) ((-2 * origin.y + yAxisLength()) / 2 - getStringWidth(chart.getYLabel(), g) / 2),
            (int) (horOffsetLeft - snip) / 2
        );

        // revert the coordinate system
        g2d.setTransform(defaultAt);
    }

    /**
     * Performs the initial calculations and sets-up the component.
     *
     * @param g the graphics object to calculate upon
     */
    private void calculate(Graphics g) {
        // convert the largest number to a string and get it's width in pixels
        int len = getStringWidth(String.valueOf(chart.getMaxY()), g);
        this.horOffsetLeft = 50 + len;

        this.origin = new Point((int) horOffsetLeft, (int) (getHeight() - vertOffsetBottom));
    }

    /**
     * Returns the length of the y-axis.
     *
     * @return the length of the y-axis
     */
    private double yAxisLength() {
        return getHeight() - vertOffsetBottom - vertOffsetTop;
    }

    /**
     * Returns the length of the x-axis.
     *
     * @return the length of the x-axis
     */
    private double xAxisLength() {
        return getWidth() - horOffsetLeft - horOffsetRight;
    }

    /**
     * Returns the rightmost x coordinate of the x-axis
     *
     * @return the x coordinate of the end point of the x-axis.
     */
    private double xEndpoint() {
        return getWidth() - horOffsetRight;
    }

    /**
     * Returns the top y coordinate of the y-axis
     *
     * @return the y coordinate of the end point of the y-axis.
     */
    private double yEndpoint() {
        return vertOffsetTop;
    }

    /**
     * Returns the width of a single bar.
     *
     * @return the width of a single bar.
     */
    private double barWidth() {
        return xAxisLength() / chart.noOfXValues();
    }

    /**
     * Returns the width of the specified string (in pixels).
     *
     * @param str the string to get the width from
     * @param g   the graphics object on which the string is
     *            to be displayed
     * @return the width of the specified string (in pixels)
     */
    private int getStringWidth(String str, Graphics g) {
        return g.getFontMetrics().stringWidth(str);
    }
}