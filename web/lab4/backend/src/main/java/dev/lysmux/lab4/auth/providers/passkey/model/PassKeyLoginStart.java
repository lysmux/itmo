package dev.lysmux.lab4.auth.providers.passkey.model;

public record PassKeyLoginStart(
        String options,
        String operationId
) {
}
