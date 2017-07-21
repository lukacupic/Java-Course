package hr.fer.zemris.java.hw06.shell;

import java.util.SortedMap;

/**
 * Represents the working environment for the {@link MyShell} program.
 * The environment provides a list of commands which the shell will be
 * able to operate with. It also provides different methods used for
 * interaction with the user, such as reading and writing from and to
 * the standard output and standard input.
 *
 * @author Luka Čupić
 */
public interface Environment {

    /**
     * Reads a line from the standard input.
     *
     * @return a string value of the read line
     * @throws ShellIOException if a reading error occurs
     */
    String readLine() throws ShellIOException;

    /**
     * Writes a string to the standard input.
     *
     * @param text the text which is to be written
     * @throws ShellIOException if a writing error occurs
     */
    void write(String text) throws ShellIOException;

    /**
     * Writes a string to the standard input, finishing
     * the output with a new-line character.
     *
     * @param text the text which is to be written
     * @throws ShellIOException if a writing error occurs
     */
    void writeln(String text) throws ShellIOException;

    /**
     * Returns a sorted map of all available commands for
     * this environment. The name of the command represents
     * the key of the map, and the corresponding {@link ShellStatus}
     * object represents the value of the map.
     *
     * @return a sorted map of all available commands for this
     * environment
     */
    SortedMap<String, ShellCommand> commands();

    /**
     * Gets the current multi-line symbol.
     *
     * @return the current multi-line symbol.
     */
    Character getMultilineSymbol();

    /**
     * Sets a new multi-line symbol.
     */
    void setMultilineSymbol(Character symbol);

    /**
     * Gets the current prompt symbol.
     *
     * @return the current prompt symbol.
     */
    Character getPromptSymbol();

    /**
     * Sets a new prompt symbol.
     */
    void setPromptSymbol(Character symbol);

    /**
     * Gets the current more-lines symbol.
     *
     * @return the current more-lines symbol.
     */
    Character getMorelinesSymbol();

    /**
     * Sets a new more-lines symbol.
     */
    void setMorelinesSymbol(Character symbol);

}
