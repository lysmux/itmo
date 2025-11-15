package dev.lysmux.lab4;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class AuthExceptionMapper implements ExceptionMapper<AuthException> {
    @Override
    public Response toResponse(AuthException exception) {
        ErrorResponse response = new ErrorResponse("unauthorized", exception.getMessage());

        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(response)
                .build();
    }
}
