package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.CommandUtil;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Represents a {@link hr.fer.zemris.java.hw06.shell.MyShell} command which
 * lists all available charsets onto the console.
 * The command receives no arguments.
 *
 * @author Luka Čupić
 */
public class CharsetsShellCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        for (String charset : Charset.availableCharsets().keySet()) {
            env.writeln(charset);
        }
        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "charsets";
    }

    @Override
    public List<String> getCommandDescription() {
        return CommandUtil.stringsToList(
            "Lists the names of supported charsets for this Java platform."
        );
    }
}
