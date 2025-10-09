package dev.lysmux.server.exception.handler;

import dev.lysmux.server.exception.ErrorResponse;

public interface ExceptionHandler<T extends Exception> {
    ErrorResponse handle(T exception);
}
