package dev.lysmux.lab6.common.command.exception;

import dev.lysmux.lab6.common.command.Command;

/**
 * Exception that throws when command could not be registered in {@link dev.lysmux.lab6.common.command.CommandRegistry}
 * <p>
 * Exception throws when command not annotated with {@link Command}
 * or does not have <b>execute</b> method or have more than <b>1</b> of them
 * </p>
 * * @see Command
 *
 * @since 1.o
 */
public class InvalidCommandException extends RuntimeException {
    /**
     * Create exception
     *
     * @param message reason
     */
    public InvalidCommandException(String message) {
        super(message);
    }
}
