package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.CommandUtil;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.util.List;

/**
 * Represents a {@link hr.fer.zemris.java.hw06.shell.MyShell} command which
 * provides help for using the commands of the shell.
 * The command receives zero or one arguments: if one argument is given, then
 * it represents the command to get the help for. If no arguments are provided,
 * then the command lists all available commands of the shell.
 *
 * @author Luka Čupić
 */
public class HelpShellCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        String[] parts = CommandUtil.trimStringArray(arguments.split(" "));

        if (parts[0].isEmpty()) {
            listCommands(env);
            return ShellStatus.CONTINUE;
        }

        if (parts.length == 1) {
            printCommand(env, parts[0]);
            return ShellStatus.CONTINUE;
        }

        env.writeln("Illegal number of arguments!");
        return ShellStatus.CONTINUE;
    }

    /**
     * Prints a list of all the available commands for the given
     * {@param env} object.
     *
     * @param env the environment whose commands are to be printed.
     */
    private void listCommands(Environment env) {
        for (String command : env.commands().keySet()) {
            env.writeln(command);
        }
    }

    /**
     * Prints the name and the description of the given command
     * by delegating the printing to an {@param env} object.
     *
     * @param env         the Environment object to delegate the printing to
     * @param commandName the name of the command to print
     */
    private void printCommand(Environment env, String commandName) {
        ShellCommand command = env.commands().get(commandName);

        if (command == null) {
            env.writeln("Unknown command!");
            return;
        }

        env.writeln(command.getCommandName());
        for (String line : command.getCommandDescription()) {
            env.writeln(line);
        }
    }

    @Override
    public String getCommandName() {
        return "help";
    }

    @Override
    public List<String> getCommandDescription() {
        return CommandUtil.stringsToList(
            "Prints the name and the description of the given command.",
            "Lists the available commands if no futher arguments are given."
        );
    }
}
