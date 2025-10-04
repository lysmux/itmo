package dev.lysmux.dto;

import jakarta.validation.constraints.Min;

public record CheckRequest(
        @Min(0)
        double x,
        double y,
        double r
) {
}
