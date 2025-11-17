package dev.lysmux.lab4.auth.providers.passkey;

public record PassKeyLoginStart(
        String options,
        String operationId
) {
}
