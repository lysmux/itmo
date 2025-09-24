package dev.lysmux.server.api.dto;

import dev.lysmux.server.domain.DotCheck;

public record CheckResponse(DotCheck[] checks, String time, long executionTime) {
}