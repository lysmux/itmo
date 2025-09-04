package dev.lysmux.fcgi.param.parser.exception;

public class ParseException extends ValidationException {
    public ParseException(String arg, Class<?> type) {
        super("Could not parse `%s` to type `%s`".formatted(arg, type.getSimpleName()));
    }
}
