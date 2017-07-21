package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.CommandUtil;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.io.File;
import java.text.ParseException;
import java.util.List;

/**
 * Represents a {@link hr.fer.zemris.java.hw06.shell.MyShell} command which
 * writes a tree-like structure of the given directory to the standard output,
 * recursively listing all files and folders under the given directory.
 * The command receives a single argument: the path of the directory whose
 * contents are to be written.
 *
 * @author Luka Čupić
 */
public class TreeShellCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        String[] parts;
        try {
            parts = CommandUtil.parseArguments(arguments);
        } catch (ParseException e) {
            env.writeln("An error has occurred while parsing the arguments!");
            return ShellStatus.CONTINUE;
        }

        if (parts.length != 1 || parts[0].isEmpty()) {
            env.writeln("Illegal number of arguments!");
            return ShellStatus.CONTINUE;
        }

        printTree(new File(parts[0]), env, 0);

        return ShellStatus.CONTINUE;
    }

    /**
     * Prints a tree-like structure of the given directory and all it's
     * subdirectories. Only directories and files are include in the output.
     *
     * @param root   the root directory to print
     * @param env    the {@link Environment} object to delegate the printing to
     * @param indent the starting indent; zero is generally the default value
     */
    private void printTree(File root, Environment env, int indent) {
        File[] files = root.listFiles();

        if (files == null) return;

        for (File current : files) {
            if (current.isDirectory()) {
                String str = String.format("%s+ %s", CommandUtil.getChars(indent, ' '), current.getName());
                env.writeln(str);

                printTree(current, env, indent + 2);
            } else if (current.isFile()) {
                String str = String.format("%s%s", CommandUtil.getChars(indent, ' '), current.getName());
                env.writeln(str);
            }
        }
    }

    @Override
    public String getCommandName() {
        return "tree";
    }

    @Override
    public List<String> getCommandDescription() {
        return CommandUtil.stringsToList(
            "Writes a tree hierarchy of the given directory"
        );
    }
}
