package dev.lysmux.lab4.auth.model;

import lombok.Builder;

@Builder
public record RefreshToken(
        String id,
        boolean banned
) {
    public RefreshToken(boolean banned) {
        this(null, banned);
    }
}
