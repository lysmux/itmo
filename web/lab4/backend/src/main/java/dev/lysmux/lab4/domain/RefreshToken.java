package dev.lysmux.lab4.domain;

public record RefreshToken(
        String id,
        boolean banned
) {
}
