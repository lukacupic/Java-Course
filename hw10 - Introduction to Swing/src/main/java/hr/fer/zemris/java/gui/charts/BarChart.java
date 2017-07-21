package hr.fer.zemris.java.gui.charts;

import java.util.List;

/**
 * This class represents a bar chart.
 * <p>
 * The bar chart is a chart that presents grouped data with
 * rectangular bars with lengths proportional to the values
 * that they represent.
 * If the given triple (yMin, yMax, distance) is such that
 * {@code yMax - yMin} does not divide {@code distance},
 * then  {@code yMax} value will be replaced with the first
 * value greater than yMax which satisfy the stated condition.
 *
 * @author Luka Čupić
 */
public class BarChart {

    /**
     * Holds the pairs of X-> values.
     */
    private List<XYValue> values;

    /**
     * The label for the x-axis.
     */
    private String xLabel;

    /**
     * The label for the y-axis.
     */
    private String yLabel;

    /**
     * The minimum value for the y-axis.
     */
    private int minY;

    /**
     * The maximum value for the y-axis.
     */
    private int maxY;

    /**
     * The distance on the y-axis between two adjacent values.
     */
    private int distance;

    /**
     * Creates a new instance of this class.
     *
     * @param values   pairs of X-> values
     * @param xLabel   label for the x-axis
     * @param yLabel   label for the y-axis
     * @param minY     minimum value for the y-axis
     * @param maxY     maximum value for the y-axis
     * @param distance distance on the y-axis between two adjacent values
     */
    public BarChart(List<XYValue> values, String xLabel, String yLabel, int minY, int maxY, int distance) {
        this.values = values;
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        this.minY = minY;
        this.maxY = maxY;
        this.distance = distance;

        if ((maxY - minY) % distance != 0) {
            this.maxY = (int) (minY + distance * Math.ceil((double) (maxY - minY) / distance));
        }
    }

    /**
     * Gets the {@link XYValue}s.
     *
     * @return the XYValue objects of this bar chart
     */
    public List<XYValue> getValues() {
        return values;
    }

    /**
     * Gets the label of the x-axis.
     *
     * @return the label of the x-axis.
     */
    public String getXLabel() {
        return xLabel;
    }

    /**
     * Gets the label of the y-axis.
     *
     * @return the label of the y-axis.
     */
    public String getYLabel() {
        return yLabel;
    }

    /**
     * Gets the minimum value of the y-axis.
     *
     * @return the minimum value of the y-axis.
     */
    public int getMinY() {
        return minY;
    }

    /**
     * Gets the maximum value of the y-axis.
     *
     * @return the maximum value of the y-axis.
     */
    public int getMaxY() {
        return maxY;
    }

    /**
     * The distance on the y-axis between two adjacent values.
     *
     * @return the distance on the y-axis between two adjacent values.
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Gets the number of values on the y-axis.
     *
     * @return the number of values on the y-axis
     */
    public int noOfYValues() {
        return (this.maxY - this.minY) / this.distance;
    }

    /**
     * Gets the number of values on the x-axis.
     *
     * @return the number of values on the x-axis
     */
    public int noOfXValues() {
        return values.size();
    }
}
