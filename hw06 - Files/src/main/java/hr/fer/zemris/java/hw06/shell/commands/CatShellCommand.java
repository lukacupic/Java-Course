package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.CommandUtil;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.List;

/**
 * Represents a {@link hr.fer.zemris.java.hw06.shell.MyShell} command which
 * writes the contents of a given file onto the console by using either a
 * given {@link Charset}, or the default charset, if none is provided.
 * <p>
 * The command receives either one or two arguments: the first argument
 * is always a path to the file. The second argument is optional and if
 * present, must contain the name of the charset which will be used to
 * interpret the bytes from the file. If the second argument is not provided,
 * a default charset will be chosen.
 *
 * @author Luka Čupić
 */
public class CatShellCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        String[] parts;
        try {
            parts = CommandUtil.parseArguments(arguments);
        } catch (ParseException e) {
            env.writeln("An error has occurred while parsing the arguments!");
            return ShellStatus.CONTINUE;
        }

        Charset charset;

        Path path;
        try {
            path = Paths.get(parts[0]);
        } catch (InvalidPathException ex) {
            env.writeln("The given file path is invalid!");
            return ShellStatus.CONTINUE;
        }

        if (parts.length == 1) {
            charset = Charset.defaultCharset();

        } else if (parts.length == 2) {
            try {
                charset = Charset.forName(parts[1]);
            } catch (Exception ex) {
                env.writeln("Illegal or unsupported charset!");
                return ShellStatus.CONTINUE;
            }

        } else {
            env.writeln("Illegal number of arguments!");
            return ShellStatus.CONTINUE;
        }

        try {
            String file = new String(Files.readAllBytes(path), charset);
            env.writeln(file);
        } catch (IOException e) {
            env.writeln("Could not read the given file!");
            return ShellStatus.CONTINUE;
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "cat";
    }

    @Override
    public List<String> getCommandDescription() {
        return CommandUtil.stringsToList(
            "Writes out the file data onto the standard output."
        );
    }
}
