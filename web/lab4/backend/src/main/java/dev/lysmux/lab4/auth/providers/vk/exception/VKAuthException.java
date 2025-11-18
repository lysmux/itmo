package dev.lysmux.lab4.auth.providers.vk.exception;

import dev.lysmux.lab4.auth.exception.AuthException;

public class VKAuthException extends AuthException {
    public VKAuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
