package hr.fer.zemris.java.hw06.shell;

import hr.fer.zemris.java.hw06.shell.commands.*;

import java.util.Collections;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Represents a Shell program which the user will interact with.
 * <p>
 * The available commands for this shell are: "cat", "copy", "exit",
 * "help", "hexdump", "ls", "mkdir", "symbol" and "tree".
 * <p>
 * The available commands and their descriptions can be listed
 * using the "help" command.
 *
 * @author Luka Čupić
 */
public class MyShell {

    /**
     * Represents the current shell environment.
     */
    private static Environment environment;

    /**
     * Represents a scanner used for reading from the standard input.
     */
    private static Scanner sc;

    /**
     * The main method.
     *
     * @param args command line arguments; as denoted in the class' Javadoc
     */
    public static void main(String[] args) {
        initShell();

        try {
            startShell();
        } catch (Exception ex) {
            System.out.println("An error has occurred while reading the user input!");
        }
    }

    /**
     * Starts this shell by greeting the user and reading from the input.
     */
    private static void startShell() {
        System.out.println("Welcome to MyShell v1.0");

        ShellCommand command;
        ShellStatus status = null;
        do {
            System.out.printf("%c ", environment.getPromptSymbol());

            // read the user input and extract the command name and the arguments
            String line = readUserInput().trim();

            String commandName = getCommandName(line).trim();
            int argumentsIndex = line.indexOf(commandName) + commandName.length();
            String arguments = line.substring(argumentsIndex).trim();

            command = environment.commands().get(commandName);
            if (command == null) {
                System.out.println("Unknown command!");
                continue;
            }

            status = command.executeCommand(environment, arguments);
        } while (status == null || status != ShellStatus.TERMINATE);
    }

    /**
     * Initializes some basic settings for this shell.
     */
    private static void initShell() {
        sc = new Scanner(System.in);
        environment = new MyEnvironment(sc);
    }

    /**
     * Extracts and returns the command name from the given string.
     *
     * @param line the string to extract the command name from
     * @return the command name extracted from the given string
     */
    private static String getCommandName(String line) {
        int index = line.indexOf(" ");

        // if the string contains no spaces, then it's just one "word"
        if (index == -1) return line;

        return line.substring(0, index);
    }

    /**
     * A helper method for reading the user input. Will read single or
     * multiple-line inputs, depending on whether the input was single-line
     * or multiple-line, as defined by appending the multi-line symbol.
     *
     * @return the user input.
     */
    private static String readUserInput() {
        StringBuilder builder = new StringBuilder();

        while (sc.hasNextLine()) {
            String line = sc.nextLine();

            builder.append(line);

            // if there's a symbol for new line, remove it and continue reading
            if (line.endsWith(String.valueOf(environment.getMorelinesSymbol()))) {
                builder.deleteCharAt(builder.length() - 1);
                continue;
            }
            break;
        }
        return builder.toString();
    }

    /**
     * Represents the current, working environment for this shell.
     *
     * @author Luka Čupić
     */
    private static class MyEnvironment implements Environment {

        /**
         * The default prompt symbol.
         */
        private char PROMPTSYMBOL = '>';

        /**
         * The symbol for writing a command in multiple lines.
         */
        private char MORELINESSYMBOL = '\\';

        /**
         * Represents a multi-line symbol.
         */
        private char MULTILINESYMBOL = '|';

        /**
         * Represents a scanner used for reading from the standard input.
         * The reference to an existing scanner is received from the {@link MyShell}
         * class through the constructor.
         */
        private Scanner sc;

        /**
         * Represents a collection of commands supportable by this
         * shell environment.
         */
        private SortedMap<String, ShellCommand> commands;

        /**
         * Initializes this environment instance.
         */
        public MyEnvironment(Scanner sc) {
            commands = new TreeMap<>();
            commands.put("charsets", new CharsetsShellCommand());
            commands.put("cat", new CatShellCommand());
            commands.put("ls", new LsShellCommand());
            commands.put("tree", new TreeShellCommand());
            commands.put("copy", new CopyShellCommand());
            commands.put("mkdir", new MkdirShellCommand());
            commands.put("hexdump", new HexdumpShellCommand());
            commands.put("exit", new ExitShellCommand());
            commands.put("symbol", new SymbolShellCommand());
            commands.put("help", new HelpShellCommand());

            this.sc = sc;
        }

        @Override
        public String readLine() throws ShellIOException {
            if (!sc.hasNextLine()) {
                throw new ShellIOException("Could not read user input!");
            }
            return sc.nextLine();
        }

        @Override
        public void write(String text) throws ShellIOException {
            System.out.print(text);
        }

        @Override
        public void writeln(String text) throws ShellIOException {
            System.out.println(text);
        }

        @Override
        public SortedMap<String, ShellCommand> commands() {
            return Collections.unmodifiableSortedMap(commands);
        }

        @Override
        public Character getMultilineSymbol() {
            return MULTILINESYMBOL;
        }

        @Override
        public void setMultilineSymbol(Character symbol) {
            MULTILINESYMBOL = symbol;
        }

        @Override
        public Character getPromptSymbol() {
            return PROMPTSYMBOL;
        }

        @Override
        public void setPromptSymbol(Character symbol) {
            PROMPTSYMBOL = symbol;
        }

        @Override
        public Character getMorelinesSymbol() {
            return MORELINESSYMBOL;
        }

        @Override
        public void setMorelinesSymbol(Character symbol) {
            MORELINESSYMBOL = symbol;
        }
    }
}