package dev.lysmux.lab4.service.hit.exception;

import dev.lysmux.lab4.core.exception.ObjectExistsException;

public class HitNotFoundException extends ObjectExistsException {
    public HitNotFoundException(String id) {
        super("Hit with id %s not found".formatted(id));
    }
}
