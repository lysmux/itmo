package dev.lysmux.lab5.io.serializer.exception;

/**
 * Exception that throws if serialization or deserialization could not be performed
 */
public class SerializeException extends Exception {
    /**
     * Create exception
     *
     * @param cause reason
     */
    public SerializeException(Throwable cause) {
        super("Could not serialize", cause);
    }
}
