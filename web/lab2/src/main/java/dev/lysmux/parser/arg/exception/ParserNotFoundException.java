package dev.lysmux.parser.arg.exception;

public class ParserNotFoundException extends RuntimeException {
    public ParserNotFoundException(Class<?> type) {
        super("Parser not found for " + type.getSimpleName());
    }
}
