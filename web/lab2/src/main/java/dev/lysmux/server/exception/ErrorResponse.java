package dev.lysmux.server.exception;

import lombok.Builder;

@Builder
public record ErrorResponse(int statusCode, ErrorDetails details) {
    public record ErrorDetails(
            String message,
            Object details
    ) {
        public ErrorDetails(String message) {
            this(message, null);
        }
    }
}
