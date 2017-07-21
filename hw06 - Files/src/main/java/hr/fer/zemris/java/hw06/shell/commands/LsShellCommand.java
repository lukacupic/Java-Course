package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.CommandUtil;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Represents a {@link hr.fer.zemris.java.hw06.shell.MyShell} command which
 * lists all files and directories in the given directory.
 * The command receives one argument: a path to the directory whose contents
 * are to be listed.
 *
 * @author Luka Čupić
 */
public class LsShellCommand implements ShellCommand {

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

        File directory = new File(parts[0]);

        if (!directory.isDirectory()) {
            env.writeln("The given argument is not a legal path!");
            return ShellStatus.CONTINUE;
        }

        return writeDescriptions(directory, env);
    }

    /**
     * Writes out the file descriptions for all files or directories contained
     * in the directory {@param directory} (not recursively!).
     *
     * @param directory the directory whose contents are to be listed
     * @param env       the environment to delegate the reading and the writing to
     * @return the status of the shell after performing this command
     */
    private ShellStatus writeDescriptions(File directory, Environment env) {
        if (!directory.isDirectory() || !directory.exists()) {
            env.writeln("The given directory path is invalid!");
            return ShellStatus.CONTINUE;
        }

        for (File file : directory.listFiles()) {

            BasicFileAttributes attrs;
            try {
                attrs = getFileAttributes(file.getPath()).readAttributes();
            } catch (Exception ex) {
                env.writeln("Error reading file attributes!");
                return ShellStatus.CONTINUE;
            }

            String drwx = getDrwx(file);
            long size = attrs.size();
            String time = getTime(attrs);
            String name = file.getName();

            String fileAttributes = formatAttributes(drwx, size, time, name);
            env.writeln(fileAttributes);

        }
        return ShellStatus.CONTINUE;
    }

    /**
     * Formats the given attributes into a string, thus returning a string
     * representation of the file's attributes.
     *
     * @param drwx the "drwx" file properties; See {@link LsShellCommand#getDrwx(File)}
     *             for more information
     * @param size the size of the file
     * @param time the formatted creation time of the file
     * @param name the file name
     * @return a formatted string representation of the given file's attributes
     */
    private String formatAttributes(String drwx, long size, String time, String name) {
        String sizeStr = String.valueOf(size);
        String spaces = CommandUtil.getChars(10 - sizeStr.length(), ' ');

        return drwx + " " + spaces + sizeStr + " " + time + " " + name;
    }

    /**
     * Returns a formatted creation time attribute of a file from it's
     * {@link BasicFileAttributes} attributes.
     *
     * @param attrs the object representing the file's attributes.
     * @return a formatted creation time attribute of a file.
     */
    private String getTime(BasicFileAttributes attrs) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        FileTime fileTime = attrs.creationTime();
        return sdf.format(new Date(fileTime.toMillis()));
    }

    /**
     * Returns a "drwx" attribute of a file. The "drwx" stands for
     * "Directory", "Readable", "Writable", "Executable". For each
     * of the four properties, either a lowercase letter of the
     * corresponding property will be written or a minus sign (-),
     * depending on whether the file satisfies a given property.
     *
     * @param file the file to return the "drwx" property from.
     * @return a four-letter string, denoting the "drwx" properties
     * of the file.
     */
    private String getDrwx(File file) {
        char[] drwx = new char[4];
        drwx[0] = file.isDirectory() ? 'd' : '-';
        drwx[1] = file.canRead() ? 'r' : '-';
        drwx[2] = file.canWrite() ? 'w' : '-';
        drwx[3] = file.canExecute() ? 'x' : '-';

        return String.valueOf(drwx);
    }

    /**
     * Returns a {@link BasicFileAttributeView} object representing the
     * attribute view of a file denoted by {@param file}, which represent
     * the path to the file.
     *
     * @param file the file whose attribute view is to be returned
     * @return a {@link BasicFileAttributeView} object representing the
     * attribute view of a file.
     */
    private BasicFileAttributeView getFileAttributes(String file) {
        Path path = Paths.get(file);
        return Files.getFileAttributeView(
            path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS
        );
    }

    @Override
    public String getCommandName() {
        return "ls";
    }

    @Override
    public List<String> getCommandDescription() {
        return CommandUtil.stringsToList(
            "Lists all items in the given folder "
                + "and provides each item's attributes."
        );
    }
}
