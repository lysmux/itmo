package dev.lysmux.lab7.common.handler.exception;

/**
 * Exception that throws if object could not be parsed
 */
public class CouldNotParseObjectException extends RuntimeException {
    /**
     * Create exception
     *
     * @param message reason
     */
    public CouldNotParseObjectException(String message) {
        super(message);
    }
}
