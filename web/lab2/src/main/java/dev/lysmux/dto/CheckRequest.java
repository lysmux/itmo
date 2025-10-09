package dev.lysmux.dto;

import jakarta.validation.constraints.Min;

public record CheckRequest(
        double[] x,
        double y,
        double[] r
) {
}
