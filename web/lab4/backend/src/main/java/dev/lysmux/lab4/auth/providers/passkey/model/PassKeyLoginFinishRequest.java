package dev.lysmux.lab4.auth.providers.passkey.model;

import com.fasterxml.jackson.databind.JsonNode;

public record PassKeyLoginFinishRequest(
        JsonNode loginResponseJSON,
        String operationId
) {
}
