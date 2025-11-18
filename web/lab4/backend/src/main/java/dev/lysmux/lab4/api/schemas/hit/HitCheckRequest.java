package dev.lysmux.lab4.api.schemas.hit;

public record HitCheckRequest(
        double x,
        double y,
        double r
) {
}
