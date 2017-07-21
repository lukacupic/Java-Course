package hr.fer.zemris.java.hw06.shell;

import java.util.List;

/**
 * Represents a command for the {@link MyShell} program.
 *
 * @author Luka Čupić
 */
public interface ShellCommand {

    /**
     * Executes this command, providing the result as described in the
     * description for each command.
     *
     * @param env       the environment object which will be used to delegate
     *                  any interaction (reading or writing) with the user
     * @param arguments the arguments of this command
     * @return the status in which the shell should, from now on, continue
     * working in.
     */
    ShellStatus executeCommand(Environment env, String arguments);

    /**
     * Returns the name of this command.
     *
     * @return the name of this command.
     */
    String getCommandName();

    /**
     * Returns a read-only list of strings, which provide the description
     * for this command.
     *
     * @return a read-only list which provides the description for this
     * command.
     */
    List<String> getCommandDescription();
}