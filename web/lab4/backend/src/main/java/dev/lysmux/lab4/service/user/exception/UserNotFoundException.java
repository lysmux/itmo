package dev.lysmux.lab4.service.user.exception;

import dev.lysmux.lab4.core.exception.ObjectNotFoundException;

public class UserNotFoundException extends ObjectNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public static UserNotFoundException byId(String userId) {
        return new UserNotFoundException("User with id %s not found".formatted(userId));
    }

    public static UserNotFoundException byUsername(String username) {
        return new UserNotFoundException("User with username %s not found".formatted(username));
    }
}
