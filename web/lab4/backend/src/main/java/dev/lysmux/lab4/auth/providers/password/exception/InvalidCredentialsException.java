package dev.lysmux.lab4.auth.providers.password.exception;

import dev.lysmux.lab4.auth.exception.AuthException;

public class InvalidCredentialsException extends AuthException {
    public InvalidCredentialsException() {
        super("Invalid username or password");
    }
}
