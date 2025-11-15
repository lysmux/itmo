package dev.lysmux.lab4.domain;

public record Token(
        String token,
        int expiresIn
) {
}
