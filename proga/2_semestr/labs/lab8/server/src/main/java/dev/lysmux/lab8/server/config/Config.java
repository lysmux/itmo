package dev.lysmux.lab8.server.config;

public record Config(
        DatabaseConfig database,
        int listenPort
) {}
