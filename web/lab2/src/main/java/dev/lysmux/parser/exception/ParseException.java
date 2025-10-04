package dev.lysmux.parser.exception;

import lombok.Getter;

public class ParseException extends RuntimeException {
    @Getter
    private final String arg;

    @Getter
    private final Class<?> type;

    public ParseException(String arg, Class<?> type) {
        super("Could not parse `%s` to type `%s`".formatted(arg, type.getSimpleName()));

        this.arg = arg;
        this.type = type;
    }
}
