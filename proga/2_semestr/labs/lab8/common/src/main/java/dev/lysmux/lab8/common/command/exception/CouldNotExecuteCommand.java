package dev.lysmux.lab8.common.command.exception;

/**
 * Exception that throws if command could not be executed with provided args
 *
 * <p>Exception may be thrown because reflection is used</p>
 *
 * @since 1.0
 */
public class CouldNotExecuteCommand extends RuntimeException {
    /**
     * Create exception
     *
     * @param cause reason
     */
    public CouldNotExecuteCommand(Throwable cause) {
        super("Could not execute command", cause);
    }
}
