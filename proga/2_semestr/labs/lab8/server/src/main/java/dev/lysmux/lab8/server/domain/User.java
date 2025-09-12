package dev.lysmux.lab8.server.domain;

public record User(
        int id,
        String login,
        String hashedPassword
) {
    public User(String login, String hashedPassword) {
        this(-1, login, hashedPassword);
    }
}
