package dev.lysmux.lab4.api.schemas.auth;

import jakarta.validation.constraints.NotBlank;

public record VKCallbackRequest(
        @NotBlank String code,
        @NotBlank String deviceId,
        @NotBlank String challengeVerifier
) {
}
