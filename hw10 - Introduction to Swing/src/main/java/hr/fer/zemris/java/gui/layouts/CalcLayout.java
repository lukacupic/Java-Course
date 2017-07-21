package hr.fer.zemris.java.gui.layouts;

import hr.fer.zemris.java.gui.calc.Calculator;

import java.awt.*;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * The {@code CalcLayout} class is a layout manager which
 * aligns components in the form of a table, with the exception
 * of the element at the top left position, which extends five
 * standard component lengths.
 * <p>
 * The default (and fixed) values for the number of rows and
 * columns are 5 and 7, respectively. This layout's intended
 * purpose is to be used as a layout for the {@link Calculator}
 * object, but it's not forbidden to use it for other objects.
 *
 * @author Luka Čupić
 */
public class CalcLayout implements LayoutManager2 {

    /**
     * Represents the number of rows for this layout.
     */
    private static final int ROWS = 5;

    /**
     * Represents the number of columns for this layout.
     */
    private static final int COLS = 7;

    /**
     * The horizontal gap (in pixels) which specifies the space
     * between both columns and rows.
     */
    private int gap;

    /**
     * Represents all components managed by this layout.
     */
    private Component[][] grid;

    /**
     * Constructs a new calc layout with no gaps between components.
     */
    public CalcLayout() {
        this(0);
    }

    /**
     * Constructs a calc layout with the specified gap (in pixels)
     * between components.
     *
     * @param gap the horizontal and vertical gap
     */
    public CalcLayout(int gap) {
        this.gap = gap;
        grid = new Component[ROWS][COLS];
    }

    @Override
    public void addLayoutComponent(Component comp, Object c) {
        Optional<RCPosition> optional = getConstraints(c);

        if (!optional.isPresent()) {
            throw new IllegalArgumentException("Illegal constraints object!");
        }

        RCPosition constraints = optional.get();

        int row = constraints.getRow();
        int col = constraints.getColumn();

        if (row < 1 || col < 1 || row > ROWS || col > COLS ||
            (row == 1 && (col >= 2 && col <= 5))) {
            throw new IllegalArgumentException("Illegal constraints position!");
        }

        if (grid[row - 1][col - 1] != null) {
            throw new IllegalArgumentException("Component already exists at the " +
                "position of the given constraints!"
            );
        }

        grid[row - 1][col - 1] = comp;

        // TODO: check if this is really necessary
        // if the (1, 1) component is added, occupy places from (1, 2) to (1, 5) too
        if (row == 1 && col == 1) {
            for (int i = 1; i <= 4; i++) {
                grid[0][i] = comp;
            }
        }
    }

    /**
     * A helper method which extracts the constraints from the
     * specified object and returns the {@link RCPosition} object,
     * representing the constraints, wrapped inside Optional object.
     *
     * @param c the object representing the constraints; can be either
     *          an instance of {@link RCPosition} or of {@link String},
     *          where the string representation must be in the form of
     *          {@code "x,y"} where x and y represent the row and the
     *          column respectively
     * @return an optional object, representing the given constraints,
     * or an {@link Optional#empty()} if the passed object was not legal
     */
    private Optional<RCPosition> getConstraints(Object c) {
        if (c instanceof RCPosition) {
            return Optional.of((RCPosition) c);
        }

        if (!(c instanceof String)) {
            return Optional.empty();
        }

        String constraints = (String) c;
        String[] values = constraints.split(",");

        if (values.length != 2) {
            return Optional.empty();
        }

        try {
            int row = Integer.parseInt(values[0]);
            int col = Integer.parseInt(values[1]);

            return Optional.of(new RCPosition(row, col));
        } catch (NumberFormatException ex) {
            return Optional.empty();
        }
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0.5f;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0.5f;
    }

    @Override
    public void invalidateLayout(Container target) {
    }

    /**
     * @deprecated replaced by <code>addLayoutComponent(Component, Object)</code>.
     */
    @Deprecated
    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (comp == grid[i][j]) {
                    comp = null;
                }
            }
        }
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return layoutSize(parent, Component::getMinimumSize, Math::max);
    }

    @Override
    public Dimension maximumLayoutSize(Container parent) {
        return layoutSize(parent, Component::getMaximumSize, Math::min);
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return layoutSize(parent, Component::getPreferredSize, Math::max);
    }

    /**
     * A helper method for determining either the minimum, maximum or preferred
     * layout size. The exact type of the wanted layout size is determined by
     * the given function whose {@link Function#apply} method must delegate to
     * either: {@link Component#getPreferredSize()}, {@link Component#getMinimumSize()}
     * or {@link Component#getMaximumSize()}. The comparison operator (whether
     * minimum or maximum value will be used) is passed as the second component,
     * which delegates to either {@link Math#min} or {@link Math#max}.
     *
     * @param parent     the parent container
     * @param function   the function which delegates the calculation to a specific
     *                   {@link Component} method
     * @param comparator the comparator function, which is either {@link Math#min} or
     *                   {@link Math#max}
     * @return the (minimum/preferred/maximum) size of this layout; the exact type
     * is determined by the given function objects
     */

    private Dimension layoutSize(Container parent, Function<Component, Dimension> function,
                                 BiFunction<Double, Double, Double> comparator) {
        int maxHeight = Integer.MIN_VALUE;
        int maxWidth = Integer.MIN_VALUE;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                Component c = grid[i][j];
                if (c == null) continue;

                Dimension dim = function.apply(c);

                if (i == 1 && (j >= 1 && j <= 5)) {
                    dim = new Dimension(dim.width / 5, dim.height);
                }

                maxHeight = comparator.apply((double) maxHeight, dim.getHeight()).intValue();
                maxWidth = comparator.apply((double) maxWidth, dim.getWidth()).intValue();
            }
        }

        Insets ins = parent.getInsets();
        return new Dimension(
            ins.left + maxWidth + ins.right + (COLS - 1) * gap,
            ins.top + maxHeight + ins.bottom + (ROWS - 1) * gap
        );
    }

    @Override
    public void layoutContainer(Container parent) {
        Insets ins = parent.getInsets();
        int componentWidth = (parent.getWidth() - ins.left - ins.right - (COLS - 1) * gap) / COLS;
        int componentHeight = (parent.getHeight() - ins.top - ins.bottom - (ROWS - 1) * gap) / ROWS;

        for (int i = 0, y = ins.top; i < grid.length; i++, y += componentHeight + gap) {
            for (int j = 0, x = ins.left; j < grid[i].length; j++, x += componentWidth + gap) {
                Component c = grid[i][j];
                if (c == null) continue;

                if (i == 0 && (j >= 1 && j <= 4)) continue;

                if (i == 0 && j == 0) {
                    c.setBounds(x, y, componentWidth * 5 + gap * 4, componentHeight);
                } else {
                    c.setBounds(x, y, componentWidth, componentHeight);
                }
            }
        }
    }

    /**
     * Gets the current gap of this layout.
     *
     * @return the current gap value (in pixels)
     */
    public int getGap() {
        return gap;
    }

    /**
     * Sets the gap for this layout.
     *
     * @param gap the new gap value (in pixels)
     */
    public void setGap(int gap) {
        this.gap = gap;
    }
}
