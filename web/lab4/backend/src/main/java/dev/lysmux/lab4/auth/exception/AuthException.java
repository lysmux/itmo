package dev.lysmux.lab4.auth.exception;

public class AuthException extends RuntimeException {
    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthException(String message) {
        super(message);
    }
}
