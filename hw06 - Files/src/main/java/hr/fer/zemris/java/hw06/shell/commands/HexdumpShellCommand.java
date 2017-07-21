package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.crypto.Util;
import hr.fer.zemris.java.hw06.shell.CommandUtil;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.List;

/**
 * A {@link hr.fer.zemris.java.hw06.shell.MyShell} command which dumps the
 * contents of a given file onto the console in a hexadecimal representation
 * via the standard output.
 * The command receives one argument: the path to the file whose contents
 * are to be dumped onto the screen.
 *
 * @author Luka Čupić
 */
public class HexdumpShellCommand implements ShellCommand {

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

        Path path;
        try {
            path = Paths.get(parts[0]);
        } catch (InvalidPathException ex) {
            env.writeln("The given file path is invalid!");
            return ShellStatus.CONTINUE;
        }

        try {
            return dumpHex(path, env);
        } catch (IOException e) {
            env.writeln("An error has occurred while dumping the file data!");
            return ShellStatus.CONTINUE;
        }
    }

    /**
     * Dumps a hexadecimal representation of the array of bytes
     * of the given file onto the standard output by delegating
     * the printing to the {@link Environment} object.
     *
     * @param path the path to the file
     * @return the status of the shell after performing this command
     * @throws IOException if an error occurs while reading the file
     */
    private ShellStatus dumpHex(Path path, Environment env) throws IOException {
        BufferedInputStream in;
        try {
            in = new BufferedInputStream(new FileInputStream(path.toString()));
        } catch (FileNotFoundException e) {
            throw new IOException(e);
        }

        int count = 0;
        while (true) {
            byte[] buffer = new byte[16];

            int value = in.read(buffer);
            if (value == -1) break;

            String line = formatLine(buffer, count);
            count += 10;
            env.writeln(line);
        }
        in.close();

        return ShellStatus.CONTINUE;
    }

    /**
     * Formats the given bytes into a new string.
     *
     * @param buffer the bytes to format
     * @param value  the value at the beginning of the string
     * @return a formatted string representing the given array
     * of bytes
     */
    private String formatLine(byte[] buffer, int value) {
        String text = new String(buffer).replaceAll("\0", "");
        buffer = text.getBytes();

        StringBuilder textBuilder = new StringBuilder(text);
        for (int i = 0, len = text.length(); i < len; i++) {
            char c = text.charAt(i);

            if (c < 32 || c > 127) {
                textBuilder.replace(i, i + 1, ".");
            }
        }
        text = textBuilder.toString();

        String hex = Util.bytetohex(buffer).toUpperCase();

        // split the hex string into substrings of length 2
        String[] parts = CommandUtil.splitToParts(hex, 2);

        // create the beginning value
        String valueStr = String.valueOf(value);
        String zeros = CommandUtil.getChars(8 - valueStr.length(), '0');
        valueStr = zeros + valueStr;

        StringBuilder builder = new StringBuilder();
        builder.append(valueStr).append(": ");
        for (int i = 0; i < 16; i++) {
            if (i < parts.length) {
                builder.append(parts[i]);
            } else {
                builder.append("  ");
            }

            if (i == 7) {
                builder.append("|");
            } else if (i == 15) {
                builder.append(" | ");
            } else {
                builder.append(" ");
            }
        }
        builder.append(text);

        return builder.toString();
    }

    @Override
    public String getCommandName() {
        return "hexdump";
    }

    @Override
    public List<String> getCommandDescription() {
        return CommandUtil.stringsToList(
            "Writes the hexadecimal representation of the "
                + "bytes read from the given file onto the standard input."
        );
    }
}