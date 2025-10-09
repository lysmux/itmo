package dev.lysmux.server.dto;

public record CheckRequest(
        double[] x,
        double y,
        double[] r
) {
}
