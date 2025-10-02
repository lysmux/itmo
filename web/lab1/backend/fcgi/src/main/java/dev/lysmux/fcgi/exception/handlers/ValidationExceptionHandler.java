package dev.lysmux.fcgi.exception.handlers;

import dev.lysmux.fcgi.dto.Response;
import dev.lysmux.fcgi.enums.HTTPStatus;
import dev.lysmux.fcgi.param.parser.exception.ValidationException;

public class ValidationExceptionHandler implements ExceptionHandler<ValidationException>{
    @Override
    public Response handle(ValidationException exception) {
        return Response.builder()
                .status(HTTPStatus.BAD_REQUEST)
                .contentType("text/plain")
                .body(exception.getMessage())
                .build();
    }
}
