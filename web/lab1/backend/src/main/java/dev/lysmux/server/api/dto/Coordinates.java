package dev.lysmux.server.api.dto;

public record Coordinates(
        double[] x,
        double y,
        double[] r
) {
}
