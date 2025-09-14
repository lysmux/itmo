package dev.lysmux.fcgi.exception.handlers;

import dev.lysmux.fcgi.dto.Response;

public interface ExceptionHandler<T extends Exception> {
    Response handle(T exception);
}
