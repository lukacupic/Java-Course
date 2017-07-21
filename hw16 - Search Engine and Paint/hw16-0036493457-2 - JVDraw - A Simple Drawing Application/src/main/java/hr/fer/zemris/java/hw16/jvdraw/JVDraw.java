package hr.fer.zemris.java.hw16.jvdraw;

import hr.fer.zemris.java.hw16.jvdraw.components.ColorInfo;
import hr.fer.zemris.java.hw16.jvdraw.components.JColorArea;
import hr.fer.zemris.java.hw16.jvdraw.components.JDrawingCanvas;
import hr.fer.zemris.java.hw16.jvdraw.files.FileActions;
import hr.fer.zemris.java.hw16.jvdraw.files.FileChanged;
import hr.fer.zemris.java.hw16.jvdraw.list.DrawingObjectListModel;
import hr.fer.zemris.java.hw16.jvdraw.list.listeners.DrawingObjectListKeyListener;
import hr.fer.zemris.java.hw16.jvdraw.list.listeners.DrawingObjectListMouseListener;
import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModelImpl;
import hr.fer.zemris.java.hw16.jvdraw.shapes.Circle;
import hr.fer.zemris.java.hw16.jvdraw.shapes.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.shapes.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.shapes.Line;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * This class is a GUI application called JVDraw, a "simple" application
 * for vector graphics. With this application, the user can draw lines,
 * circles and filled circles. The drawn image can be saved into the native
 * {@code *.jvd} (Java Vector Drawing) file format or it can be exported into
 * an image (such as JPG, PNG or GIF).
 *
 * @author Luka Čupić
 */
public class JVDraw extends JFrame {

	/**
	 * The drawing model.
	 */
	private DrawingModel model = new DrawingModelImpl();

	/**
	 * The provider for the foreground color.
	 */
	private JColorArea fgColorProvider = new JColorArea(Color.RED);

	/**
	 * The provider for the background color.
	 */
	private JColorArea bgColorProvider = new JColorArea(Color.BLACK);

	/**
	 * Represents the canvas used for drawing geometric objects.
	 */
	private JDrawingCanvas canvas = new JDrawingCanvas(model, fgColorProvider, bgColorProvider);

	/**
	 * Creates a new {@link JVDraw} program.
	 */
	public JVDraw() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(1000, 600);
		setVisible(true);
		setTitle("JVDraw v1.0");

		initGUI();
	}

	/**
	 * Initializes the GUI and sets up some other stuff.
	 */
	private void initGUI() {
		FileActions.setModel(model);
		new FileChanged(model);

		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());

		// initialize the toolbar
		JToolBar toolbar = createToolbar(fgColorProvider, bgColorProvider);
		cp.add(toolbar, BorderLayout.PAGE_START);

		// initialize the canvas
		canvas.setCurrentObjectType(Line.class);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});
		cp.add(canvas, BorderLayout.CENTER);

		// initialize the ColorInfo label
		ColorInfo colorInfo = new ColorInfo(fgColorProvider, bgColorProvider);
		cp.add(colorInfo, BorderLayout.PAGE_END);

		// initialize the list of objects
		JScrollPane scrollPane = new JScrollPane(createList());
		scrollPane.setPreferredSize(new Dimension(getWidth() / 5, getHeight()));
		cp.add(scrollPane, BorderLayout.LINE_END);

		// initialize the menu bar
		JMenuBar menuBar = createMenuBar();
		this.setJMenuBar(menuBar);
	}


	// Toolbar initialization

	/**
	 * Creates the toolbar with color choosers and drawing tools.
	 *
	 * @param fgArea the foreground color provider
	 * @param bgArea the background color provider
	 * @return an object representing the toolbar
	 */
	private JToolBar createToolbar(JColorArea fgArea, JColorArea bgArea) {
		JToolBar toolbar = new JToolBar();
		toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));

		toolbar.add(fgArea);
		toolbar.add(bgArea);

		addTools(toolbar);

		return toolbar;
	}

	/**
	 * Adds the drawing tools onto the provided toolbar.
	 *
	 * @param toolbar the toolbar to add the tools to
	 */
	private void addTools(JToolBar toolbar) {

		JToggleButton lineButton = new JToggleButton("Line");
		lineButton.addActionListener(e -> canvas.setCurrentObjectType(Line.class));
		toolbar.add(lineButton);
		lineButton.setSelected(true);

		JToggleButton circleButton = new JToggleButton("Circle");
		circleButton.addActionListener(e -> canvas.setCurrentObjectType(Circle.class));
		toolbar.add(circleButton);

		JToggleButton filledCircleButton = new JToggleButton("Filled Circle");
		filledCircleButton.addActionListener(e -> canvas.setCurrentObjectType(FilledCircle.class));
		toolbar.add(filledCircleButton);

		ButtonGroup group = new ButtonGroup();
		group.add(lineButton);
		group.add(circleButton);
		group.add(filledCircleButton);
	}

	// JList initialization

	/**
	 * Creates a JList object which will represent a list of geometric
	 * object from the drawing model.
	 *
	 * @return a JList object, representing a list of geometric objects
	 */
	private JList<GeometricalObject> createList() {
		JList<GeometricalObject> list = new JList<>(new DrawingObjectListModel(model));
		list.addMouseListener(new DrawingObjectListMouseListener(model));
		list.addKeyListener(new DrawingObjectListKeyListener(model));
		return list;
	}

	// JMenuBar initialization

	/**
	 * Creates the menu bar.
	 *
	 * @return an object representing the menu bar
	 */
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(createFileMenu());
		return menuBar;
	}

	/**
	 * Creates the file menu.
	 *
	 * @return an object representing the file menu
	 */
	private JMenu createFileMenu() {
		JMenu fileMenu = new JMenu("File");

		JMenuItem open = new JMenuItem(FileActions.openFile);
		fileMenu.add(open);

		JMenuItem save = new JMenuItem(FileActions.saveFile);
		fileMenu.add(save);

		JMenuItem saveAs = new JMenuItem(FileActions.saveFileAs);
		fileMenu.add(saveAs);

		JMenuItem export = new JMenuItem(FileActions.export);
		fileMenu.add(export);

		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(e -> exit());
		fileMenu.add(exit);

		return fileMenu;
	}

	// Additional initialization

	/**
	 * Exits the program.
	 */
	private void exit() {
		if (FileActions.isSaved()) {
			System.exit(0);
		} else {

			int pressed = JOptionPane.showConfirmDialog(
				null,
				"Your drawing contains changes. Do you want to save them?",
				"Save changes?",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE
			);

			if (pressed == JOptionPane.YES_OPTION) {
				FileActions.saveFile.actionPerformed(null);
			} else {
				System.exit(0);
			}
		}
	}

	/**
	 * The main method.
	 *
	 * @param args command line arguments; not used in this program
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(JVDraw::new);
	}
}