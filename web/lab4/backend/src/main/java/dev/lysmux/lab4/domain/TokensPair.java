package dev.lysmux.lab4.domain;

public record TokensPair(
        Token accessToken,
        Token refreshToken
) {
}
