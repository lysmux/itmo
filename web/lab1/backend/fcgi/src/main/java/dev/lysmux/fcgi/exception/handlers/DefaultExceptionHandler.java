package dev.lysmux.fcgi.exception.handlers;

import dev.lysmux.fcgi.dto.Response;
import dev.lysmux.fcgi.enums.HTTPStatus;

import java.util.Arrays;
import java.util.stream.Collectors;

public class DefaultExceptionHandler<T extends Exception> implements ExceptionHandler<T>{
    @Override
    public Response handle(T exception) {
        String stackTrace = Arrays.stream(exception.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n"));
        String message = exception.getMessage() + "\n" + stackTrace;

        return Response.builder()
                .status(HTTPStatus.INTERNAL_SERVER_ERROR)
                .contentType("text/plain")
                .body(message)
                .build();
    }
}
