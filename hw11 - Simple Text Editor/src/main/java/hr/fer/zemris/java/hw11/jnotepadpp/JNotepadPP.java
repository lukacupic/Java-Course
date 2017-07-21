package hr.fer.zemris.java.hw11.jnotepadpp;

import hr.fer.zemris.java.hw11.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LJMenu;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizableAction;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This class represents a "simple" text editor. JNotepad++ supports
 * opening files, editing, saving them etc. It offers a set of tools
 * used to manipulate the text, such as inverting the characters.
 * JNotepad++ offers some statistical information about the files,
 * as well as a nice clock where you can see the current time.
 * This editor offers localization, in a sense that it allows the
 * user to specify the language in which the interface will be shown.
 *
 * @author Luka Čupić
 */
public class JNotepadPP extends JFrame {

    /**
     * Represents the form localization provider, which is used
     * to fetch the localization values, i.e. translations.
     */
    private final FormLocalizationProvider flp =
        new FormLocalizationProvider(LocalizationProvider.getInstance(), this);

    /**
     * Represents the relative path to the icons folder.
     */
    private static final String ICONS_PATH = "icons/";

    /**
     * Represents the name of this program.
     */
    private static final String PROGRAM_NAME = "JNotepad++";

    /**
     * The icon which represents saved files.
     */
    private ImageIcon iconSaved;

    /**
     * The icon which represents unsaved files.
     */
    private ImageIcon iconUnsaved;

    /**
     * Represents the tabbed pane of the program, which holds
     * the editor tabs.
     */
    private JTabbedPane tabbedPane;

    /**
     * Represents a map where each component (editor) maps to
     * it's corresponding path, which represents a path to the
     * file which is being edited by the editor.
     */
    private Map<Component, Path> paths;

    /**
     * Represents a map where each component (editor) maps to
     * it's corresponding boolean, which represents whether the
     * file opened in the editor is saved.
     */
    private Map<Component, Boolean> saved;

    /**
     * Represents the current status bar of the program.
     */
    private JToolBar statusBar;

    /**
     * A label which represents the number of characters of the
     * current document.
     */
    private JLabel length;

    /**
     * A label which represents the current line of the document
     * being edited, i.e. the line in which the caret is located.
     */
    private JLabel line;

    /**
     * A label which represents the current column of the document
     * being edited, i.e. the column in which the caret is located.
     */
    private JLabel column;

    /**
     * A label which counts the characters of the selected text.
     */
    private JLabel select;

    /**
     * A flag which denotes whether the program clock should be active
     * or not.
     */
    private volatile boolean ticking;

    /**
     * The constructor.
     */
    public JNotepadPP() {
        flp.fire(); // initialize the default language

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setTitle(PROGRAM_NAME);
        setVisible(true);

        initSettings();
        initGUI();
    }

    /**
     * Initializes miscellaneous settings for the notepad.
     */
    private void initSettings() {
        tabbedPane = new JTabbedPane();
        paths = new HashMap<>();
        saved = new HashMap<>();
        statusBar = initStatusBar();
        ticking = true;

        iconSaved = getIcon(Paths.get(ICONS_PATH + "tab_saved.png"));
        iconUnsaved = getIcon(Paths.get(ICONS_PATH + "tab_unsaved.png"));
    }

    /**
     * Initializes the status bar.
     *
     * @return a new status bar with default values (0).
     */
    private JToolBar initStatusBar() {
        JToolBar statusbar = new JToolBar();
        statusbar.setLayout(new GridLayout(1, 3));

        // add left component
        length = new JLabel("length : 0");
        statusbar.add(length);

        // add center components
        JPanel center = new JPanel(new FlowLayout());
        center.setOpaque(false);

        line = new JLabel("Ln : 0");
        column = new JLabel("Col : 0");
        select = new JLabel("Sel : 0");

        center.add(line);
        center.add(column);
        center.add(select);

        statusbar.add(center);

        // add right component
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setOpaque(false);

        JLabel time = new JLabel("00:00");
        right.add(time);

        statusbar.add(right);

        // create the timer
        Thread clock = new Thread(() -> {
            while (ticking) {
                Date date = Calendar.getInstance().getTime();
                String text = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date);
                SwingUtilities.invokeLater(() -> time.setText(text));

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignorable) {
                }
            }
        });
        clock.start();

        return statusbar;
    }

    /**
     * Initializes the GUI.
     */
    private void initGUI() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        //createActions(); all actions are created in instance initializers
        createMenus();
        createToolbars();

        tabbedPane.addChangeListener(e -> {
            updatePath();
            checkActions();
            updateStatusBar();
        });
        checkActions();

        cp.add(tabbedPane, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                die();
            }
        });
    }

    /**
     * Terminates the program and kills all threads associated with it.
     */
    private void die() {
        //kill all tabs
        while (tabbedPane.getTabCount() > 0) {
            closeTab();
        }

        // terminate the clock thread
        ticking = false;

        dispose();
    }

    /**
     * Updates the status bar by calling the {@link CaretListener
     * #caretUpdate} method upon the existing CaretEvent listener.
     * Each calculation is delegated to the existing caret object.
     */
    private void updateStatusBar() {
        JTextArea editor = currentEditor();
        if (editor == null) return;

        editor.getCaretListeners()[0].caretUpdate(new CaretEvent(editor) {

            @Override
            public int getDot() {
                return editor.getCaret().getDot();
            }

            @Override
            public int getMark() {
                return editor.getCaret().getMark();
            }
        });
    }

    /**
     * Checks whether each of the actions should currently
     * be enabled or disabled. For example, if no tabs are
     * currently opened, actions such as cut, copy, and paste
     * should be disabled since they offer no functionality.
     */
    private void checkActions() {
        boolean doTabsExist = tabbedPane.getTabCount() > 0;

        JTextArea editor = currentEditor();
        boolean isEditorEmpty = editor != null && editor.getText().length() == 0;

        saveDocumentAction.setEnabled(doTabsExist);
        saveDocumentAsAction.setEnabled(doTabsExist);
        sortAscendingAction.setEnabled(doTabsExist && !isEditorEmpty);
        closeDocumentAction.setEnabled(doTabsExist);

        cutTextAction.setEnabled(doTabsExist && !isEditorEmpty);
        copyTextAction.setEnabled(doTabsExist && !isEditorEmpty);
        pasteTextAction.setEnabled(doTabsExist);

        getStatisticsAction.setEnabled(doTabsExist);

        toUpperCaseAction.setEnabled(doTabsExist && !isEditorEmpty);
        toLowerCaseAction.setEnabled(doTabsExist && !isEditorEmpty);
        invertCaseAction.setEnabled(doTabsExist && !isEditorEmpty);

        sortAscendingAction.setEnabled(doTabsExist && !isEditorEmpty);
        sortDescendingAction.setEnabled(doTabsExist && !isEditorEmpty);
        uniqueLinesAction.setEnabled(doTabsExist && !isEditorEmpty);
    }

    /**
     * Creates the menus for the notepad.
     */
    private void createMenus() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new LJMenu("file", flp);
        menuBar.add(fileMenu);

        fileMenu.add(new JMenuItem(newDocumentAction));
        fileMenu.add(new JMenuItem(openDocumentAction));
        fileMenu.add(new JMenuItem(saveDocumentAction));
        fileMenu.add(new JMenuItem(saveDocumentAsAction));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(closeDocumentAction));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(exitAction));

        JMenu editMenu = new LJMenu("edit", flp);
        menuBar.add(editMenu);

        editMenu.add(new JMenuItem(cutTextAction));
        editMenu.add(new JMenuItem(copyTextAction));
        editMenu.add(new JMenuItem(pasteTextAction));
        setJMenuBar(menuBar);

        JMenu toolsMenu = new LJMenu("tools", flp);
        menuBar.add(toolsMenu);

        JMenu changeCase = new LJMenu("changeCase", flp);
        toolsMenu.add(changeCase);

        changeCase.add(new JMenuItem(toUpperCaseAction));
        changeCase.add(new JMenuItem(toLowerCaseAction));
        changeCase.add(new JMenuItem(invertCaseAction));

        JMenu sort = new LJMenu("sort", flp);
        toolsMenu.add(sort);

        sort.add(new JMenuItem(sortAscendingAction));
        sort.add(new JMenuItem(sortDescendingAction));

        toolsMenu.add(new JMenuItem(uniqueLinesAction));
        toolsMenu.add(new JMenuItem(getStatisticsAction));

        JMenu language = new LJMenu("languages", flp);
        menuBar.add(language);

        language.add(new JMenuItem(englishLanguage));
        language.add(new JMenuItem(germanLanguage));
        language.add(new JMenuItem(croatianLanguage));

        JMenu help = new LJMenu("help", flp);
        menuBar.add(help);

        help.add(new JMenuItem(aboutAction));
    }

    /**
     * Creates the toolbars for the notepad.
     */
    private void createToolbars() {
        JToolBar toolbar = new JToolBar();

        toolbar.add(new JButton(newDocumentAction));
        toolbar.add(new JButton(openDocumentAction));
        toolbar.add(new JButton(saveDocumentAction));
        toolbar.add(new JButton(saveDocumentAsAction));
        toolbar.add(new JButton(closeDocumentAction));
        toolbar.add(new JButton(exitAction));
        toolbar.addSeparator();

        toolbar.add(new JButton(cutTextAction));
        toolbar.add(new JButton(copyTextAction));
        toolbar.add(new JButton(pasteTextAction));
        toolbar.addSeparator();

        toolbar.add(new JButton(getStatisticsAction));
        toolbar.addSeparator();

        toolbar.add(new JButton(toUpperCaseAction));
        toolbar.add(new JButton(toLowerCaseAction));
        toolbar.add(new JButton(invertCaseAction));
        toolbar.addSeparator();

        toolbar.add(new JButton(sortAscendingAction));
        toolbar.add(new JButton(sortDescendingAction));
        toolbar.add(new JButton(uniqueLinesAction));
        toolbar.addSeparator();

        toolbar.add(new JButton(englishLanguage));
        toolbar.add(new JButton(germanLanguage));
        toolbar.add(new JButton(croatianLanguage));

        getContentPane().add(toolbar, BorderLayout.PAGE_START);
    }

    /**
     * Represents an action for creating a new document.
     */
    private final Action newDocumentAction = new LocalizableAction("new", flp) {

        // instance initializer
        {
            flp.addLocalizationListener(() -> {
                putValue(Action.SHORT_DESCRIPTION, flp.getString("newDescription"));
            });

            initAction(this,
                KeyStroke.getKeyStroke("control N"),
                KeyEvent.VK_O
            );
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            createEmptyTab();
            tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
            paths.put(currentEditor(), null);
            updateSaved(true);

            openDocumentAction.putValue(Action.SHORT_DESCRIPTION, flp.getString("openDescription"));
        }
    };

    /**
     * Represents an action for opening an existing document.
     */
    private final Action openDocumentAction = new LocalizableAction("open", flp) {

        // instance initializer
        {
            flp.addLocalizationListener(() -> {
                putValue(Action.SHORT_DESCRIPTION, flp.getString("openDescription"));
            });

            initAction(this,
                KeyStroke.getKeyStroke("control O"),
                KeyEvent.VK_O
            );
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle(flp.getString("openFileTitle"));
            if (fc.showOpenDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File fileName = fc.getSelectedFile();
            Path filePath = fileName.toPath();

            if (!Files.isReadable(filePath)) {
                showErrorMessage(flp.getString("openFileError"));
                return;
            }

            try {
                byte[] data = Files.readAllBytes(filePath);

                String text = new String(data, StandardCharsets.UTF_8);
                createEmptyTab();
                tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
                currentEditor().setText(text);
                addPath(filePath);
            } catch (IOException ex) {
                showErrorMessage(flp.getString("openFileError"));
            }
            updatePath();
            updateSaved(true);
            tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
        }
    };

    /**
     * Represents an action for saving a document.
     */
    private final Action saveDocumentAction = new LocalizableAction("save", flp) {

        // instance initializer
        {
            flp.addLocalizationListener(() -> {
                putValue(Action.SHORT_DESCRIPTION, flp.getString("saveDescription"));
            });

            initAction(this,
                KeyStroke.getKeyStroke("control S"),
                KeyEvent.VK_S
            );
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentPath() == null) {
                saveDocumentAsAction.actionPerformed(e);
                return;
            }

            saveFile();
            updatePath();
            updateSaved(true);
        }
    };

    /**
     * Represents an action for saving a document under a new name.
     */
    private Action saveDocumentAsAction = new LocalizableAction("saveAs", flp) {

        // instance initializer
        {
            flp.addLocalizationListener(() -> {
                putValue(Action.SHORT_DESCRIPTION, flp.getString("saveAsDescription"));
            });

            initAction(this,
                KeyStroke.getKeyStroke("control A"),
                KeyEvent.VK_A
            );
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle(flp.getString("saveFileTitle"));
            if (fc.showSaveDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File file = fc.getSelectedFile();
            paths.put(currentEditor(), file.toPath());

            if (file.exists()) {
                int pressed = JOptionPane.showConfirmDialog(
                    JNotepadPP.this,
                    file.getName() + " " + flp.getString("replaceFileQuestion"),
                    flp.getString("confirmSaveAs"),
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );

                if (pressed == JOptionPane.YES_OPTION) {
                    saveFile();
                } else {
                    this.actionPerformed(e);
                }
            } else {
                saveFile();
            }
            updatePath();
            updateSaved(true);
        }
    };

    /**
     * Represents an action which converts the selected text from the
     * document to upper case.
     */
    private final Action toUpperCaseAction = new LocalizableAction("toUpperCase", flp) {

        // instance initializer
        {
            flp.addLocalizationListener(() -> {
                putValue(Action.SHORT_DESCRIPTION, flp.getString("toUpperCaseDescription"));
            });

            initAction(this,
                KeyStroke.getKeyStroke("control U"),
                KeyEvent.VK_U
            );
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JTextArea editor = currentEditor();
            if (editor == null) return;
            Document doc = editor.getDocument();

            int offset = 0;
            int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());
            if (len == 0) {
                len = doc.getLength();
            } else {
                offset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
            }

            try {
                String text = doc.getText(offset, len);
                doc.remove(offset, len);
                doc.insertString(offset, text.toUpperCase(), null);
            } catch (BadLocationException ignorable) {
            }
        }
    };

    /**
     * Represents an action which converts the selected text from the
     * document to lower case.
     */
    private final Action toLowerCaseAction = new LocalizableAction("toLowerCase", flp) {

        // instance initializer
        {
            flp.addLocalizationListener(() -> {
                putValue(Action.SHORT_DESCRIPTION, flp.getString("toLowerCaseDescription"));
            });

            initAction(this,
                KeyStroke.getKeyStroke("control L"),
                KeyEvent.VK_L
            );
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JTextArea editor = currentEditor();
            if (editor == null) return;
            Document doc = editor.getDocument();

            int offset = 0;
            int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());
            if (len == 0) {
                len = doc.getLength();
            } else {
                offset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
            }

            try {
                String text = doc.getText(offset, len);
                doc.remove(offset, len);
                doc.insertString(offset, text.toLowerCase(), null);
            } catch (BadLocationException ignorable) {
            }
        }
    };

    /**
     * Represents an action which toggles the selected text in the document,
     * by replacing all upper-case letters with lower-case, and vice versa.
     */
    private final Action invertCaseAction = new LocalizableAction("invertCase", flp) {

        // instance initializer
        {
            flp.addLocalizationListener(() -> {
                putValue(Action.SHORT_DESCRIPTION, flp.getString("invertCaseDescription"));
            });

            initAction(this,
                KeyStroke.getKeyStroke("control I"),
                KeyEvent.VK_I
            );
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JTextArea editor = currentEditor();
            if (editor == null) return;
            Document doc = editor.getDocument();

            int offset = 0;
            int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());
            if (len == 0) {
                len = doc.getLength();
            } else {
                offset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
            }

            try {
                String text = doc.getText(offset, len);
                doc.remove(offset, len);
                doc.insertString(offset, invertText(text), null);
            } catch (BadLocationException ignorable) {
            }
        }

        /**
         * Inverts the given text, by replacing all upper-case letters with
         * lower-case letters, and vice versa.
         *
         * @param text the text to invert
         * @return the inverted text
         */
        private String invertText(String text) {
            StringBuilder sb = new StringBuilder(text.length());
            for (char c : text.toCharArray()) {
                if (Character.isUpperCase(c)) {
                    sb.append(Character.toLowerCase(c));
                } else if (Character.isLowerCase(c)) {
                    sb.append(Character.toUpperCase(c));
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        }
    };

    /**
     * Represents an action for closing the currently opened document.
     */
    private final Action closeDocumentAction = new LocalizableAction("close", flp) {

        // instance initializer
        {
            flp.addLocalizationListener(() -> {
                putValue(Action.SHORT_DESCRIPTION, flp.getString("closeDescription"));
            });

            initAction(this,
                KeyStroke.getKeyStroke("control W"),
                KeyEvent.VK_E
            );
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            closeTab();
        }
    };

    /**
     * Represents an action for exiting the application.
     */
    private final Action exitAction = new LocalizableAction("exit", flp) {

        // instance initializer
        {
            flp.addLocalizationListener(() -> {
                putValue(Action.SHORT_DESCRIPTION, flp.getString("exitDescription"));
            });

            initAction(this,
                KeyStroke.getKeyStroke("control ESC"),
                KeyEvent.VK_E
            );
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    };

    /**
     * Represents an action for cutting the selected text
     * adn storing it to the system clipboard.
     */
    private final Action cutTextAction = new LocalizableAction("cut", flp) {

        // instance initializer
        {
            flp.addLocalizationListener(() -> {
                putValue(Action.SHORT_DESCRIPTION, flp.getString("cutDescription"));
            });

            initAction(this,
                KeyStroke.getKeyStroke("control X"),
                KeyEvent.VK_C
            );
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JTextArea editor = currentEditor();
            if (editor == null) return;
            editor.cut();
        }
    };

    /**
     * Represents an action for copying the selected text to
     * the system clipboard.
     */
    private final Action copyTextAction = new LocalizableAction("copy", flp) {

        // instance initializer
        {
            flp.addLocalizationListener(() -> {
                putValue(Action.SHORT_DESCRIPTION, flp.getString("copyDescription"));
            });

            initAction(this,
                KeyStroke.getKeyStroke("control C"),
                KeyEvent.VK_C
            );
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JTextArea editor = currentEditor();
            if (editor == null) return;
            editor.copy();
        }
    };

    /**
     * Represents an action for pasting the contents (text) from
     * the system clipboard onto the current editor.
     */
    private final Action pasteTextAction = new LocalizableAction("paste", flp) {

        // instance initializer
        {
            flp.addLocalizationListener(() -> {
                putValue(Action.SHORT_DESCRIPTION, flp.getString("pasteDescription"));
            });

            initAction(this,
                KeyStroke.getKeyStroke("control V"),
                KeyEvent.VK_V
            );
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JTextArea editor = currentEditor();
            if (editor == null) return;
            editor.paste();
        }
    };

    /**
     * Sorts the selected lines of the currently active document
     * in the ascending order.
     */
    private final Action sortAscendingAction = new LocalizableAction("ascending", flp) {

        // instance initializer
        {
            flp.addLocalizationListener(() -> {
                putValue(Action.SHORT_DESCRIPTION, flp.getString("ascendingDescription"));
            });

            initAction(this,
                KeyStroke.getKeyStroke("control A"),
                KeyEvent.VK_A
            );
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            sortLines(true);
        }
    };

    /**
     * Sorts the selected lines of the currently active document
     * in the descending order.
     */
    private final Action sortDescendingAction = new LocalizableAction("descending", flp) {

        // instance initializer
        {
            flp.addLocalizationListener(() -> {
                putValue(Action.SHORT_DESCRIPTION, flp.getString("descendingDescription"));
            });

            initAction(this,
                KeyStroke.getKeyStroke("control D"),
                KeyEvent.VK_D
            );
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            sortLines(false);
        }
    };

    /**
     * Represents an action which removes all duplicate lines from the
     * selected document text.
     */
    private final Action uniqueLinesAction = new LocalizableAction("unique", flp) {

        // instance initializer
        {
            flp.addLocalizationListener(() -> {
                putValue(Action.SHORT_DESCRIPTION, flp.getString("uniqueDescription"));
            });

            initAction(this,
                KeyStroke.getKeyStroke("control U"),
                KeyEvent.VK_U
            );
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            modifyLines(lines -> lines.stream()
                .distinct()
                .collect(Collectors.toList()));
        }
    };

    /**
     * Represents an action for getting the statistics about the
     * currently active document. Provided statistics are as follows:
     * the total number of characters in the document; the number of
     * non-blank characters in the document; the number of lines in
     * the document.
     */
    private final Action getStatisticsAction = new LocalizableAction("statistics", flp) {

        // instance initializer
        {
            flp.addLocalizationListener(() -> {
                putValue(Action.SHORT_DESCRIPTION, flp.getString("statisticsDescription"));
            });

            initAction(this,
                KeyStroke.getKeyStroke("control I"),
                KeyEvent.VK_I
            );
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JTextArea editor = currentEditor();
            if (editor == null) return;

            String text = editor.getText();
            int noOfChars = text.length();
            int nonBlanks = text.replaceAll("\\s+", "").length();
            int noOfLines = editor.getLineCount();

            JOptionPane.showMessageDialog(
                JNotepadPP.this,
                String.format(
                    flp.getString("statisticsText"),
                    noOfChars,
                    nonBlanks,
                    noOfLines
                ),
                flp.getString("statisticsTitle"),
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    };

    /**
     * Represents an action for providing information about the
     * program.
     */
    private final Action aboutAction = new LocalizableAction("about", flp) {

        // instance initializer
        {
            flp.addLocalizationListener(() -> {
                putValue(Action.SHORT_DESCRIPTION, flp.getString("aboutDescription"));
            });

            initAction(this,
                KeyStroke.getKeyStroke("F12"),
                KeyEvent.VK_F12
            );
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(
                JNotepadPP.this,
                flp.getString("aboutMessage"),
                flp.getString("about"),
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    };

    /**
     * Represents an action which updates the current language to English.
     */
    private final Action englishLanguage = new LocalizableAction("english", flp) {

        // instance initializer
        {
            flp.addLocalizationListener(() -> {
                putValue(Action.SHORT_DESCRIPTION, flp.getString("englishDescription"));
            });

            initAction(this,
                KeyStroke.getKeyStroke("control E"),
                KeyEvent.VK_E
            );
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            LocalizationProvider.getInstance().setLanguage("en");
        }
    };

    /**
     * Represents an action which updates the current language to German.
     */
    private final Action germanLanguage = new LocalizableAction("german", flp) {

        // instance initializer
        {
            flp.addLocalizationListener(() -> {
                putValue(Action.SHORT_DESCRIPTION, flp.getString("germanDescription"));
            });

            initAction(this,
                KeyStroke.getKeyStroke("control G"),
                KeyEvent.VK_G
            );
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            LocalizationProvider.getInstance().setLanguage("de");
        }
    };

    /**
     * Represents an action which updates the current language to Croatian.
     */
    private final Action croatianLanguage = new LocalizableAction("croatian", flp) {

        // instance initializer
        {
            flp.addLocalizationListener(() -> {
                putValue(Action.SHORT_DESCRIPTION, flp.getString("croatianDescription"));
            });

            initAction(this,
                KeyStroke.getKeyStroke("control R"),
                KeyEvent.VK_R
            );
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            LocalizationProvider.getInstance().setLanguage("hr");
        }
    };

    /**
     * A helper method for sorting the selected lines of the currently
     * active document.
     *
     * @param isAscending a flag which specifies whether the sorting should be
     *                    performed in the ascending or descending order
     */
    private void sortLines(boolean isAscending) {
        Locale locale = LocalizationProvider.getInstance().getLocale();
        Collator collator = Collator.getInstance(locale);

        modifyLines(lines -> lines.stream()
            .sorted((s1, s2) -> (isAscending ? 1 : -1) * collator.compare(s1, s2))
            .collect(Collectors.toList()));
    }

    /**
     * A helper method which performs an action upon the selected lines
     * of the currently active document. The action to perform is specified
     * by the given function object.
     *
     * @param stream a function which specified the action to be performed.
     *               It should either: sort strings in the ascending order,
     *               sort strings in the descending order, or select distinct
     *               strings
     */
    private void modifyLines(Function<List<String>, List<String>> stream) {
        JTextArea editor = currentEditor();
        if (editor == null) return;

        int dotReal = editor.getCaret().getDot();
        int markReal = editor.getCaret().getMark();

        // if the selection was made backwards, treat it as if it wasn't
        int dot = Math.min(dotReal, markReal);
        int mark = Math.max(dotReal, markReal);

        try {
            int firstLine = editor.getLineOfOffset(dot);
            int lastLine = editor.getLineOfOffset(mark);

            List<String> lines = new ArrayList<>();

            String[] text = editor.getText().split("\\r?\\n");
            for (int i = 0; i < text.length; i++) {
                if (i < firstLine) continue;
                if (i > lastLine) break;

                lines.add(text[i] + "\n");
            }

            List<String> result = stream.apply(lines);

            int firstLineOffset = editor.getLineStartOffset(firstLine);
            int lastLineOffset = editor.getLineEndOffset(lastLine);

            editor.getDocument().remove(firstLineOffset, lastLineOffset - firstLineOffset);

            StringBuilder sb = new StringBuilder();
            for (String line : result) {
                sb.append(line);
            }
            editor.insert(sb.toString(), firstLineOffset);

        } catch (BadLocationException ignorable) {
        }
    }

    /**
     * Initializes the following attributes for the given action: name, accelerator
     * key, mnemonic key and short description.
     *
     * @param a           the action to initialize the attributes for
     * @param accelerator the accelerator key
     * @param mnemonic    the mnemonic key
     */
    private void initAction(Action a, KeyStroke accelerator, int mnemonic) {
        a.putValue(Action.ACCELERATOR_KEY, accelerator);
        a.putValue(Action.MNEMONIC_KEY, mnemonic);
    }

    /**
     * A helper method for saving the currently opened file.
     * Contents which will be saved are taken from the
     * {@link #currentEditor()} and saved in a file specified
     * by {@link #currentPath()}.
     */
    private void saveFile() {
        JTextArea editor = currentEditor();
        if (editor == null) return;

        try {
            Files.write(currentPath(), editor.getText().getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            paths.put(currentEditor(), null);
            showErrorMessage(flp.getString("fileSavingError"));
        }
    }

    /**
     * Returns the currently active text editor.
     *
     * @return the currently active text editor, which can be {@code null}
     */
    private JTextArea currentEditor() {
        JScrollPane pane = ((JScrollPane) tabbedPane.getSelectedComponent());
        if (pane == null) return null;

        JViewport viewport = pane.getViewport();
        return (JTextArea) viewport.getView();
    }

    /**
     * Returns the path to the file opened in the currently
     * active text editor.
     *
     * @return the path to the file opened in the currently
     * active text editor, which can be {@code null}
     */
    private Path currentPath() {
        return paths.get(currentEditor());
    }

    /**
     * Creates a new, empty tab and adds it to the {@link #tabbedPane}.
     */
    private void createEmptyTab() {
        JTextArea editor = new JTextArea();

        editor.addCaretListener(e -> {
            JTextArea editor1 = (JTextArea) e.getSource();
            try {
                int ln = editor1.getLineOfOffset(e.getDot());
                int col = e.getDot() - editor1.getLineStartOffset(ln);

                length.setText("length : " + String.valueOf(editor1.getText().length()));
                line.setText("Ln : " + String.valueOf(ln + 1));
                column.setText("Col : " + String.valueOf(col));
                select.setText("Sel : " + Math.abs(e.getDot() - e.getMark()));
            } catch (BadLocationException ignorable) {
            }
        });

        editor.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentChanged();
            }

            /**
             * Handles the changes made to this document.
             */
            private void documentChanged() {
                updateSaved(false);
                checkActions();
            }
        });

        tabbedPane.addTab("untitled", iconSaved, new JScrollPane(editor));
        saved.put(currentEditor(), true);

        getContentPane().add(statusBar, BorderLayout.PAGE_END);
    }

    /**
     * Updates the flag for the current editor, which indicates whether the
     * document which is currently being edited has been saved or not.
     *
     * @param isSaved a flag which indicates whether the current document
     *                has been saved or not
     */
    private void updateSaved(boolean isSaved) {
        saved.put(currentEditor(), isSaved);
        tabbedPane.setIconAt(tabbedPane.getSelectedIndex(), isSaved ? iconSaved : iconUnsaved);

    }

    /**
     * Adds the specified path as the path of the current editor.
     * This means that the currently opened document is not untitled,
     * since it "maps" to an existing path on the disk.
     *
     * @param filePath the path to the file, which is opened in the
     *                 currently active editor
     */
    private void addPath(Path filePath) {
        paths.put(currentEditor(), filePath);
    }

    /**
     * Updates the window title to match the path of the file which
     * is currently being edited, and also updates the title of the
     * currently active tab.
     */
    private void updatePath() {
        int index = tabbedPane.getSelectedIndex();
        Path currentPath = currentPath();

        if (currentPath == null) {
            if (tabbedPane.getTabCount() > 0) {
                setTitle(tabbedPane.getTitleAt(index) + " - " + PROGRAM_NAME);
            }
            return;
        }

        tabbedPane.setTitleAt(index, currentPath.getFileName().toString());
        tabbedPane.setToolTipTextAt(index, currentPath.toString());
        setTitle(currentPath.toString() + " - " + PROGRAM_NAME);
    }

    /**
     * Closes the currently active tab, and queries the user if any
     * unsaved changes should be saved.
     */
    private void closeTab() {
        JTextArea editor = currentEditor();
        if (editor == null) return;

        boolean isSaved = saved.get(editor);

        if (!isSaved) {
            String filename = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
            int pressed = JOptionPane.showConfirmDialog(
                JNotepadPP.this,
                "'" + filename + "' " + flp.getString("saveChanges"),
                flp.getString("saveFileQuestion"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

            if (pressed == JOptionPane.YES_OPTION) {
                saveDocumentAction.actionPerformed(null);
            }
        }
        tabbedPane.remove(tabbedPane.getSelectedIndex());
    }

    /**
     * Creates a message dialog which displays a generic message
     * to the user.
     *
     * @param message the message to display to the user
     */
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(
            JNotepadPP.this,
            message,
            flp.getString("errorTitle"),
            JOptionPane.ERROR_MESSAGE
        );
    }

    /**
     * Gets the icon located at the specified path.
     *
     * @param path the path of the icon to return
     * @return an {@link ImageIcon} object representing
     * the icon
     */
    private ImageIcon getIcon(Path path) {
        try {
            InputStream is = this.getClass().getResourceAsStream(path.toString());
            if (is == null) {
                throw new IOException();
            }
            byte[] bytes = Utility.readBytes(is);
            is.close();
            return new ImageIcon(bytes);
        } catch (IOException ex) {
            System.exit(1);
            return null; // IDE not recognizing System.exit as a final operation
        }
    }

    /**
     * The main method.
     *
     * @param args command line arguments; not used in this program
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(JNotepadPP::new);
    }
}