package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.CommandUtil;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.List;

/**
 * Represents a {@link hr.fer.zemris.java.hw06.shell.MyShell} command which
 * creates a new directory denoted by the given path
 * The command receives one argument: the path on which a new directory will
 * be created. If the directory already exits, the command will report an error.
 *
 * @author Luka Čupić
 */
public class MkdirShellCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        String[] parts;
        try {
            parts = CommandUtil.parseArguments(arguments);
        } catch (ParseException e) {
            env.writeln("An error has occurred while parsing the arguments!");
            return ShellStatus.CONTINUE;
        }

        if (parts.length != 1) {
            env.writeln("Illegal number of arguments!");
            return ShellStatus.CONTINUE;
        }

        try {
            Files.createDirectory(Paths.get(parts[0]));
            env.writeln("Directory successfully created!");
        } catch (IOException e) {
            env.writeln("An error has occurred while creating the directory!");
        }
        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "mkdir";
    }

    @Override
    public List<String> getCommandDescription() {
        return CommandUtil.stringsToList(
            "Creates a new directory at the specified path."
        );
    }
}
