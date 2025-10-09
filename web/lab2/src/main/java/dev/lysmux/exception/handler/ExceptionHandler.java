package dev.lysmux.exception.handler;

import dev.lysmux.exception.ErrorResponse;

public interface ExceptionHandler<T extends Exception> {
    ErrorResponse handle(T exception);
}
