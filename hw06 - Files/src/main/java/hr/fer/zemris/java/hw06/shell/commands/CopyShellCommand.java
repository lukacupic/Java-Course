package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.CommandUtil;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.io.*;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.List;

/**
 * Represents a {@link hr.fer.zemris.java.hw06.shell.MyShell} command which
 * copies the given file to another location. The command receives two arguments:
 * the source path and the destination path. If the destination path represents
 * a directory, the file will be copied into the chosen directory with the
 * original name. If the destination file already exists, the command will ask
 * the user to confirm replacing the existing file.
 *
 * @author Luka Čupić
 */
public class CopyShellCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        String[] parts;
        try {
            parts = CommandUtil.parseArguments(arguments);
        } catch (ParseException e) {
            env.writeln("An error has occurred while parsing the arguments!");
            return ShellStatus.CONTINUE;
        }

        if (parts.length != 2 || parts[0].isEmpty()) {
            env.writeln("Illegal number of arguments!");
            return ShellStatus.CONTINUE;
        }

        return copyFile(parts[0], parts[1], env);
    }

    /**
     * Copies the file on the path denoted by {@param source} to
     * the path denoted by {@param destination}. The {@param destination}
     * path can represent either a file (even an existing one - in that case,
     * the user will be asked to confirm the overwriting of the file) or a folder.
     *
     * @param source      the source path of the file
     * @param destination the destination path of the file
     * @param env         the environment to delegate the reading and the writing to
     */
    private ShellStatus copyFile(String source, String destination, Environment env) {

        File src = new File(source);
        File dest = new File(destination);

        if (!src.isFile()) {
            env.writeln("The source path does not represent a file!");
            return ShellStatus.CONTINUE;
        }

        if (dest.isDirectory() || !dest.exists()) {
            File realDest = Paths.get(destination, src.getName()).toFile();
            return copyFileForReal(src, realDest, env);
        }

        if (dest.isFile() || !dest.exists()) {
            // if the file doesn't exist, just overwrite it
            if (!dest.exists()) {
                return copyFileForReal(src, dest, env);
            }

            // if the file already exists, ask for overwriting
            String question = String.format(
                "%s already exits. Are you sure you want to replace it?",
                dest.getName()
            );
            env.writeln(question);
            env.write(env.getPromptSymbol() + " ");

            while (true) {
                String response = env.readLine();

                if ("yes".equals(response.toLowerCase())) {
                    return copyFileForReal(src, dest, env);
                }
                if ("no".equals(response.toLowerCase())) {
                    env.writeln("Alright. The file won't be replaced.");
                    return ShellStatus.CONTINUE;
                }
                env.writeln("Why are you doing this?");
            }
        }

        env.writeln("The given destination path is not valid!");
        return ShellStatus.CONTINUE;
    }

    /**
     * Copies the file if the user has confirmed it.
     *
     * @param src  the source path of the file
     * @param dest the destination path of the file
     * @param env  the environment to delegate any reading or writing to
     */
    private ShellStatus copyFileForReal(File src, File dest, Environment env) {
        BufferedInputStream in;
        BufferedOutputStream out;

        try {
            in = new BufferedInputStream(new FileInputStream(src.toString()));
            out = new BufferedOutputStream(new FileOutputStream(dest.toString()));
        } catch (FileNotFoundException e) {
            env.writeln("An error has occurred while copying the file!");
            return ShellStatus.CONTINUE;
        }

        while (true) {
            byte[] buffer = new byte[1024];

            int value;
            try {
                value = in.read(buffer);
                if (value == -1) break;

                // remove the excess null characters
                buffer = new String(buffer).replaceAll("\0", "").getBytes();

                out.write(buffer);
            } catch (IOException ex) {
                env.writeln("An error has occurred while copying the file!");
                return ShellStatus.CONTINUE;
            }
        }

        // flush any remaining bytes
        try {
            out.flush();

            in.close();
            out.close();
        } catch (IOException ignorable) {
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "copy";
    }

    @Override
    public List<String> getCommandDescription() {
        return CommandUtil.stringsToList("" +
            "Copies the file from source to the destination path."
        );
    }
}
