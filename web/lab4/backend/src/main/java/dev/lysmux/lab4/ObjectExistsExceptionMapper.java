package dev.lysmux.lab4;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ObjectExistsExceptionMapper implements ExceptionMapper<ObjectExistsException> {
    @Override
    public Response toResponse(ObjectExistsException exception) {
        ErrorResponse response = new ErrorResponse("conflict", exception.getMessage());

        return Response.status(Response.Status.CONFLICT)
                .entity(response)
                .build();
    }
}
