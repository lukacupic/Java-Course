package hr.fer.zemris.java.gui.calc;

import hr.fer.zemris.java.gui.layouts.CalcLayout;

import javax.swing.*;
import java.awt.*;

/**
 * This is a utility class used by the {@link Calculator} class.
 * It contains a single method, {@link #initCalc}, which performs
 * the initialization of the given calculator.
 * <p>
 * Initialization means the following: The predefined {@link CalcLayout}
 * is created; the screen and all the buttons, along with all their
 * operations are added to the layout; the created CalcLayout which
 * contains all previously described elements is placed onto a panel
 * which then represents the main calculator layout.
 *
 * @author Luka Čupić
 */
public class CalcUtil {

    /**
     * Represents the main color used in the calculator.
     */
    private static final Color MAIN_COLOR = new Color(100, 149, 237);

    /**
     * Represents the secondary color used in the calculator.
     */
    private static final Color SIDE_COLOR = new Color(255, 190, 0);

    /**
     * Initializes the given calculator, sets all of it's essential
     * fields to proper values, and returns a new panel representing
     * the calculator layout.
     *
     * @param calc the calculator to initialize
     * @return a new panel representing the new layout of the calculator
     */
    public static JPanel initCalc(Calculator calc) {
        JPanel panel = new JPanel();
        panel.setLayout(new CalcLayout(8));

        addScreen(panel, calc);
        addDigits(panel, calc);
        addOperators(panel, calc);
        addOperations(panel, calc);
        addFunctions(panel, calc);
        addMisc(panel, calc);

        return panel;
    }

    /**
     * Creates a new screen on the panel and adds it to the
     * calculator.
     *
     * @param panel the panel
     * @param calc  the calculator
     */
    private static void addScreen(JPanel panel, Calculator calc) {
        JLabel screen = new JLabel("0", SwingConstants.RIGHT);
        screen.setOpaque(true);
        screen.setBackground(SIDE_COLOR);
        screen.setFont(new Font("Arial", Font.BOLD, 22));
        screen.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));

        panel.add(screen, "1,1");
        calc.setScreen(screen);
    }

    /**
     * Creates the digit buttons on the panel and adds them to the
     * calculator.
     *
     * @param panel the panel
     * @param calc  the calculator
     */
    private static void addDigits(JPanel panel, Calculator calc) {
        calc.setDigits(new JButton[10]);

        JButton[] digits = calc.getDigits();

        for (int i = 0; i < 10; i++) {
            digits[i] = new JButton(String.valueOf(i));

            final int value = i;
            digits[i].addActionListener(l -> calc.appendToOperand(value));
        }

        setupButtons(panel, calc.getDigits(), new String[]{
            "5,3", "4,3", "4,4", "4,5", "3,3", "3,4", "3,5", "2,3", "2,4", "2,5",
        });
    }

    /**
     * Creates the operator buttons on the panel and adds them to the
     * calculator.
     *
     * @param panel the panel
     * @param calc  the calculator
     */
    private static void addOperators(JPanel panel, Calculator calc) {
        calc.setOperators(new JButton[]{
            new JButton("+"),
            new JButton("-"),
            new JButton("*"),
            new JButton("/"),
            new JButton("x^n")
        });

        JButton[] operators = calc.getOperators();
        for (int i = 0; i < 4; i++) {
            final int j = i;
            operators[i].addActionListener(l -> {
                calc.updateOperator(operators[j].getText());
            });
        }
        operators[4].addActionListener(l -> {
            calc.updateOperator(!calc.isInverse() ? "x^n" : "(inv)x^n");
        });

        setupButtons(panel, calc.getOperators(), new String[]{
            "5,6", "4,6", "3,6", "2,6", "5,1"
        });
    }

    /**
     * Creates the operation buttons on the panel and adds them to the
     * calculator.
     *
     * @param panel the panel
     * @param calc  the calculator
     */
    private static void addOperations(JPanel panel, Calculator calc) {
        calc.setOperations(new AbstractButton[]{
            new JButton("clr"),
            new JButton("res"),
            new JButton("push"),
            new JButton("pop"),
            new JCheckBox("inv")
        });

        AbstractButton[] operations = calc.getOperations();

        operations[0].addActionListener(l -> calc.clear());
        operations[1].addActionListener(l -> calc.reset());
        operations[2].addActionListener(l -> calc.push());
        operations[3].addActionListener(l -> calc.pop());
        operations[4].addActionListener(l -> {
            calc.setInverse(!calc.isInverse());
            invertLayout(calc);
        });

        setupButtons(panel, calc.getOperations(), new String[]{
            "1,7", "2,7", "3,7", "4,7", "5,7"
        });
    }

    /**
     * Creates the function buttons on the panel and adds them to the
     * calculator.
     *
     * @param panel the panel
     * @param calc  the calculator
     */
    private static void addFunctions(JPanel panel, Calculator calc) {
        calc.setFunctions(new JButton[]{
            new JButton("1/x"),
            new JButton("log"),
            new JButton("ln"),
            new JButton("sin"),
            new JButton("cos"),
            new JButton("tan"),
            new JButton("ctg"),
        });

        JButton[] funcs = calc.getFunctions();

        funcs[0].addActionListener(l -> {
            calc.applyFunction(d -> 1 / d);
        });
        funcs[1].addActionListener(l -> {
            calc.applyFunction(d -> calc.isInverse() ? Math.pow(10, d) : Math.log10(d));
        });
        funcs[2].addActionListener(l -> {
            calc.applyFunction((d) -> calc.isInverse() ? Math.exp(d) : Math.log(d));
        });
        funcs[3].addActionListener(l -> {
            calc.applyFunction((d) -> calc.isInverse() ? Math.asin(d) : Math.sin(d));
        });
        funcs[4].addActionListener(l -> {
            calc.applyFunction((d) -> calc.isInverse() ? Math.acos(d) : Math.cos(d));
        });
        funcs[5].addActionListener(l -> {
            calc.applyFunction((d) -> calc.isInverse() ? Math.atan(d) : Math.tan(d));
        });
        funcs[6].addActionListener(l -> {
            calc.applyFunction((d) -> calc.isInverse() ? Math.atan(1 / d) : 1 / Math.tan(d));
        });

        setupButtons(panel, funcs, new String[]{
            "2,1", "3,1", "4,1", "2,2", "3,2", "4,2", "5,2"
        });
    }

    /**
     * Creates the miscellaneous buttons on the panel and adds them to the
     * calculator.
     *
     * @param panel the panel
     * @param calc  the calculator
     */
    private static void addMisc(JPanel panel, Calculator calc) {
        calc.setMisc(new JButton[]{
            new JButton("+/-"),
            new JButton("."),
            new JButton("="),
        });

        JButton[] misc = calc.getMisc();

        misc[0].addActionListener(l -> calc.updateOperandSign());
        misc[1].addActionListener(l -> calc.setDecimal(true));
        misc[2].addActionListener(l -> calc.performOperation());

        setupButtons(panel, misc, new String[]{
            "5,5", "5,4", "1,6"
        });
    }

    /**
     * A helper method which adds the specified constraints to each button,
     * updates the color for each button and adds each button to the panel.
     *
     * @param panel       the panel
     * @param buttons     the array of buttons
     * @param constraints the constraints; "2,4" specifies row2, column4
     */
    private static void setupButtons(JPanel panel, AbstractButton[] buttons, String[] constraints) {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setOpaque(true);
            buttons[i].setBackground(MAIN_COLOR);
            buttons[i].setFont(new Font("Arial", Font.BOLD, 15));
            buttons[i].setMargin(new Insets(0, 0, 0, 0));

            panel.add(buttons[i], constraints[i]);
        }
    }

    /**
     * A helper method for inverting the calculator layout; triggered
     * when the proper calculator button has been pressed.
     *
     * @param calc the calculator
     */
    private static void invertLayout(Calculator calc) {
        calc.getScreen().setBackground(calc.isInverse() ? MAIN_COLOR : SIDE_COLOR);
        setColor(calc.getButtons(), calc.isInverse() ? SIDE_COLOR : MAIN_COLOR);

        calc.getOperators()[4].setText(calc.isInverse() ? "n√x" : "x^n");

        JButton[] funcs = calc.getFunctions();
        funcs[1].setText(calc.isInverse() ? "10^x" : "log");
        funcs[2].setText(calc.isInverse() ? "e^x" : "ln");
        funcs[3].setText(calc.isInverse() ? "arc sin" : "sin");
        funcs[4].setText(calc.isInverse() ? "arc cos" : "cos");
        funcs[5].setText(calc.isInverse() ? "arc tan" : "tan");
        funcs[6].setText(calc.isInverse() ? "arc ctg" : "ctg");
    }

    /**
     * A helper method for setting the specified color for the given
     * array of buttons.
     *
     * @param buttons the array of buttons
     * @param c       the color
     */
    private static void setColor(AbstractButton[] buttons, Color c) {
        for (AbstractButton b : buttons) {
            b.setBackground(c);
        }
    }
}
