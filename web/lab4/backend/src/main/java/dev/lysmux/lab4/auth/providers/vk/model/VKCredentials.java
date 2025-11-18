package dev.lysmux.lab4.auth.providers.vk.model;

public record VKCredentials(
        String code,
        String deviceId,
        String challengeVerifier
) {
}
