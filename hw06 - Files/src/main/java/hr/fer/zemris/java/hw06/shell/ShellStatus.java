package hr.fer.zemris.java.hw06.shell;

/**
 * Represents the current status of the {@link MyShell}.
 * The status is returned by a command, providing the shell
 * with the feedback information about whether the shell
 * should continue working or not. The most obvious example is for the
 * {@link hr.fer.zemris.java.hw06.shell.commands.ExitShellCommand} which
 * will return the {@link ShellStatus#TERMINATE} status.
 */
public enum ShellStatus {

    /**
     * Indicates that the shell should continue obtaining
     * the user input.
     */
    CONTINUE,

    /**
     * Indicates that the shell must be terminated immediately.
     */
    TERMINATE
}
