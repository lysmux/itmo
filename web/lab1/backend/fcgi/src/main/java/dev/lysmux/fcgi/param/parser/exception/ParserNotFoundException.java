package dev.lysmux.fcgi.param.parser.exception;

public class ParserNotFoundException extends ValidationException {
    public ParserNotFoundException(Class<?> type) {
        super("Parser not found for " + type.getSimpleName());
    }
}
