package dev.lysmux.lab4.api.schemas;

public record ApiResponse<T>(
        String message,
        T details
) {
    public ApiResponse(String message) {
        this(message, null);
    }
}
