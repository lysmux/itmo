package dev.lysmux.lab4;

public class UserExistsException extends ObjectExistsException {
    public UserExistsException(String username) {
        super("User with username %s already exists".formatted(username));
    }
}
