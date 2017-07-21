package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.CommandUtil;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.util.List;

/**
 * Represents a {@link hr.fer.zemris.java.hw06.shell.MyShell} command which
 * either writes, or replaces a single shell symbol.
 * The command receives one or two arguments: The first argument is always
 * the name of the symbol which will be used. If the second argument is not
 * provided, the command then writes the value of this symbol. If the second
 * argument is given, then it must represent a new value for the symbol, which
 * will then be replaced.
 *
 * @author Luka Čupić
 */
public class SymbolShellCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        String[] parts = CommandUtil.trimStringArray(arguments.split(" "));

        // if there's just one argument, write it's value to the user
        if (parts.length == 1) {
            writeSymbol(parts[0], env);
        } else if (parts.length == 2) {
            replaceSymbol(parts[0], parts[1], env);
        } else {
            env.writeln("Illegal number of arguments!");
        }
        return ShellStatus.CONTINUE;
    }

    /**
     * Replaces a symbol, denoted by it's name, by a new symbol, denoted by it's value.
     *
     * @param symbolName      the name of the symbol to replace
     * @param newSymbolString the new value of the symbol
     * @param env             the environment object to delegate the writing to
     */
    private void replaceSymbol(String symbolName, String newSymbolString, Environment env) {
        if (newSymbolString.length() != 1) {
            env.writeln("New symbol must be a single character!");
            return;
        }

        // prepare the message template
        String output = "Symbol for %s changed from '%c' to '%c'";

        char newSymbolChar = newSymbolString.charAt(0);
        switch (symbolName) {
            case "PROMPT":
                output = String.format(output, "PROMPT", env.getPromptSymbol(), newSymbolChar);
                env.setPromptSymbol(newSymbolChar);
                break;
            case "MORELINES":
                output = String.format(output, "MORELINES", env.getMorelinesSymbol(), newSymbolChar);
                env.setMorelinesSymbol(newSymbolChar);
                break;
            case "MULTILINE":
                output = String.format(output, "MULTILINE", env.getMultilineSymbol(), newSymbolChar);
                env.setMultilineSymbol(newSymbolChar);
                break;
            default:
                env.writeln("Unknown symbol!");
                return;
        }
        env.writeln(output);
    }

    /**
     * Writes a symbol, denoted by it's name, onto the standard output.
     *
     * @param symbolName the name of the symbol to write
     * @param env        the environment object to delegate the writing to
     */
    private void writeSymbol(String symbolName, Environment env) {
        // prepare the message template
        String output = "Symbol for %s is '%s'";

        switch (symbolName) {
            case "PROMPT":
                output = String.format(output, "PROMPT", env.getPromptSymbol());
                break;
            case "MORELINES":
                output = String.format(output, "MORELINES", env.getMorelinesSymbol());
                break;
            case "MULTILINE":
                output = String.format(output, "MULTILINE", env.getMultilineSymbol());
                break;
            default:
                env.writeln("Unknown symbol!");
                return;
        }
        env.writeln(output);
    }

    @Override
    public String getCommandName() {
        return "symbol";
    }

    @Override
    public List<String> getCommandDescription() {
        return null;
    }
}