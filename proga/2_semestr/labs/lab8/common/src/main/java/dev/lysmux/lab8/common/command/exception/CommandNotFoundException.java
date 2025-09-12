package dev.lysmux.lab8.common.command.exception;

/**
 * Exception that throws if command not found
 *
 * @since 1.0
 */
public class CommandNotFoundException extends Exception {
    /**
     * Create exception
     *
     * @param commandName name of command that not found
     */
    public CommandNotFoundException(String commandName) {
        super("Command `%s` not found".formatted(commandName));
    }
}
