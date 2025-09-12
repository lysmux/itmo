package dev.lysmux.lab8.common.handler.parser;

/**
 * Exception that throws if {@code input} could not be converted to {@code type}
 */
public class CouldNotParseArgException extends Exception {
    /**
     * Create exception
     *
     * @param input        input value
     * @param requiredType type to convert input
     */
    public CouldNotParseArgException(String input, String requiredType) {
        super("Could not parse `%s` to type `%s`".formatted(input, requiredType));
    }
}
