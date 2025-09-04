package dev.lysmux.fcgi.dto;

import dev.lysmux.fcgi.enums.HTTPMethod;
import dev.lysmux.fcgi.enums.HTTPStatus;

import java.lang.reflect.Method;

public record Route(
        HTTPMethod method,
        String path,
        HTTPStatus status,
        Method handler,
        Object controller
) {
}
