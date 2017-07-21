package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.CommandUtil;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.util.List;

/**
 * Represents a {@link hr.fer.zemris.java.hw06.shell.MyShell} command which
 * terminates currently active shell program.
 * The command receives no arguments.
 *
 * @author Luka Čupić
 */
public class ExitShellCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        return ShellStatus.TERMINATE;
    }

    @Override
    public String getCommandName() {
        return "exit";
    }

    @Override
    public List<String> getCommandDescription() {
        return CommandUtil.stringsToList("Terminates the shell.");
    }
}
