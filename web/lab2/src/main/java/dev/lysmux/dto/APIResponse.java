package dev.lysmux.dto;

import dev.lysmux.ResponseStatus;

public record APIResponse<T extends Record>(
        ResponseStatus status,
        T result
) {
}
