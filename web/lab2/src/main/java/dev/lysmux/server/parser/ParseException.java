package dev.lysmux.server.parser;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class ParseException extends RuntimeException {
    @Getter
    private final Map<String, String[]> fieldViolations;

    public ParseException(String message) {
        this(message, new HashMap<>());
    }

    public ParseException(String message, Map<String, String[]> fieldViolations) {
        super(message);

        this.fieldViolations = fieldViolations;
    }
}
