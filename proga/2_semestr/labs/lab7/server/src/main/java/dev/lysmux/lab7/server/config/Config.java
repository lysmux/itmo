package dev.lysmux.lab7.server.config;

public record Config(
        DatabaseConfig database,
        int listenPort
) {}
