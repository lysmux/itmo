package dev.lysmux.lab4.domain.model;

import lombok.Builder;

@Builder
public record HitResult(
        String id,
        String ownerId,
        double x,
        double y,
        double r,
        boolean hit,
        long executionTime,
        String time
) {
}
