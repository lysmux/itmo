package dev.lysmux.lab5.controller.exception;

/**
 * Exception that throws when command could not be registered in {@link dev.lysmux.lab5.controller.CommandManager}
 * <p>
 * Exception throws when command not annotated with {@link dev.lysmux.lab5.controller.Command}
 * or does not have <b>execute</b> method or have more than <b>1</b> of them
 * </p>
 *
 * @see dev.lysmux.lab5.controller.CommandManager
 * @see dev.lysmux.lab5.controller.Command
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
