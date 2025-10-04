package dev.lysmux.dto;

import lombok.Builder;

@Builder
public record ErrorResponse<T>(
        String type,
        String message,
        T details
) {
}
