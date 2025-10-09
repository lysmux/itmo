package dev.lysmux.server.exception.handler;

import dev.lysmux.server.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;

public class DefaultExceptionHandler<T extends Exception> implements ExceptionHandler<T> {
    @Override
    public ErrorResponse handle(T exception) {
        return new ErrorResponse(
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                new ErrorResponse.ErrorDetails(
                        exception.getMessage(),
                        Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList()
                )
        );
    }
}
