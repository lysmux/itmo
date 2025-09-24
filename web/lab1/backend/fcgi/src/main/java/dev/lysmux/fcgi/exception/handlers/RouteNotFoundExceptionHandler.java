package dev.lysmux.fcgi.exception.handlers;

import dev.lysmux.fcgi.dto.Response;
import dev.lysmux.fcgi.enums.HTTPStatus;
import dev.lysmux.fcgi.exception.RouteNotFoundException;

public class RouteNotFoundExceptionHandler implements ExceptionHandler<RouteNotFoundException> {

    @Override
    public Response handle(RouteNotFoundException exception) {
        return Response.builder()
                .status(HTTPStatus.NOT_FOUND)
                .contentType("text/plain")
                .body(exception.getMessage())
                .build();
    }
}
