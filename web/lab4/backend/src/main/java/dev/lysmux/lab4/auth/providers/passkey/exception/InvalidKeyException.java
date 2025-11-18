package dev.lysmux.lab4.auth.providers.passkey.exception;

import dev.lysmux.lab4.auth.exception.AuthException;

public class InvalidKeyException extends AuthException {
    public InvalidKeyException() {
        super("Could not verify pass key");
    }
}
