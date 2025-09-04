package dev.lysmux.fcgi.param.parser.exception;

import dev.lysmux.fcgi.param.ArrayParameter;
import dev.lysmux.fcgi.param.MapParameter;

public class InvalidValueException extends ValidationException {
    public InvalidValueException(String paramName, Class<?> actualType, Class<?> neededType) {
        super("Invalid for `%s` with type `%s`, actual `%s`".formatted(paramName, formatTypeName(neededType), formatTypeName(actualType)));
    }

    private static String formatTypeName(Class<?> type) {
        if (type.isArray() || type.isAssignableFrom(ArrayParameter.class)) return "Array";
        else if (type.isRecord() || type.isAssignableFrom(MapParameter.class)) return "Object";
        else return type.getSimpleName();
    }
}
