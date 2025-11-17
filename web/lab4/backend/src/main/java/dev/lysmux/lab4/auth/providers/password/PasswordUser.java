package dev.lysmux.lab4.auth.providers.password;

public record PasswordUser(
        String userId,
        String username,
        String password
) {
}
