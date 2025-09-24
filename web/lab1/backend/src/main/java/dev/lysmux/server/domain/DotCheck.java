package dev.lysmux.server.domain;

import lombok.Builder;

@Builder
public record DotCheck(double x, double y, double r, boolean contains) {
}
