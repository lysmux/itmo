package dev.lysmux.parser.arg.exception;

import dev.lysmux.parser.ParseException;
import lombok.Getter;

public class ArgParseException extends ParseException {
    @Getter
    private final String arg;

    @Getter
    private final Class<?> type;

    public ArgParseException(String arg, Class<?> type) {
        super("Could not parse `%s` to type `%s`".formatted(arg, type.getSimpleName()));

        this.arg = arg;
        this.type = type;
    }
}
