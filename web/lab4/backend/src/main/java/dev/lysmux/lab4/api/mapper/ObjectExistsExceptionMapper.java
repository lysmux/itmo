package dev.lysmux.lab4.api.mapper;

import dev.lysmux.lab4.api.schemas.ApiResponse;
import dev.lysmux.lab4.core.exception.ObjectExistsException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ObjectExistsExceptionMapper implements ExceptionMapper<ObjectExistsException> {
    @Override
    public Response toResponse(ObjectExistsException exception) {
        ApiResponse<Void> response = new ApiResponse<>(exception.getMessage());

        return Response.status(Response.Status.CONFLICT)
                .entity(response)
                .build();
    }
}
