package dev.lysmux.lab4.service.user.exception;

import dev.lysmux.lab4.core.exception.ObjectExistsException;

public class UserExistsException extends ObjectExistsException {
    public UserExistsException(String username) {
        super("User with username %s already exists".formatted(username));
    }
}
