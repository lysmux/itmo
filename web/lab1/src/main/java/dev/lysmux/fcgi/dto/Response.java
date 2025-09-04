package dev.lysmux.fcgi.dto;

import dev.lysmux.fcgi.enums.HTTPStatus;
import lombok.Builder;
import lombok.Singular;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

@Builder
public record Response(
        HTTPStatus status,
        String contentType,
        @Singular Map<String, Object> headers,
        String body
) {
    private final static String httpVersion = "1.1";

    public String toString() {
        String headers = this.headers.entrySet().stream()
                        .map((entry) -> "%s: %s".formatted(entry.getKey(), entry.getValue().toString()))
                        .collect(Collectors.joining("\n"));

        return """
                HTTP/%s %d %s
                Content-Type: %s
                Content-Length: %d
                %s
                %s
                """.formatted(
                httpVersion,
                status.getCode(),
                status.getReasonPhrase(),
                contentType,
                body.getBytes(StandardCharsets.UTF_8).length,
                headers.isEmpty() ? "" : headers + "\n",
                body
        );
    }
}
