package dev.lysmux.lab4.api.mapper;

import dev.lysmux.lab4.api.schemas.ApiResponse;
import dev.lysmux.lab4.core.exception.ObjectNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ObjectNotFoundExceptionMapper implements ExceptionMapper<ObjectNotFoundException> {
    @Override
    public Response toResponse(ObjectNotFoundException exception) {
        ApiResponse<Void> response = new ApiResponse<>(exception.getMessage());

        return Response.status(Response.Status.NOT_FOUND)
                .entity(response)
                .build();
    }
}
