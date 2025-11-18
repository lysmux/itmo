package dev.lysmux.lab4.auth.model;

public record Token(
        String token,
        int expiresIn
) {
}
