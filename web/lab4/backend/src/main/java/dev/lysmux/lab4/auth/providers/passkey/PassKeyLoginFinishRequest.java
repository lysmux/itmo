package dev.lysmux.lab4.auth.providers.passkey;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;

public record PassKeyLoginFinishRequest(
        JsonNode loginResponseJSON,
        String operationId
) {
}
