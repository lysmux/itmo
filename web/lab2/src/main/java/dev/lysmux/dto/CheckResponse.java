package dev.lysmux.dto;

import lombok.Builder;

@Builder
public record CheckResponse(double x, double y, double r) {
}
