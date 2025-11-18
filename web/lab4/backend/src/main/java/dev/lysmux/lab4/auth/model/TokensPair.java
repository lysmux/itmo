package dev.lysmux.lab4.auth.model;

public record TokensPair(
        Token accessToken,
        Token refreshToken
) {
}
