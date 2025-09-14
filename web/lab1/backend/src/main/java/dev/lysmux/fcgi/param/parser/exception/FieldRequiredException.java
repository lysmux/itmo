package dev.lysmux.fcgi.param.parser.exception;

public class FieldRequiredException extends ValidationException {
    public FieldRequiredException(String fieldName) {
        super("Field `%s` required, but not exists".formatted(fieldName));
    }
}
