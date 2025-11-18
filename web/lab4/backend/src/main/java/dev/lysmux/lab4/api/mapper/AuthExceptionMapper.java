package dev.lysmux.lab4.api.mapper;

import dev.lysmux.lab4.api.schemas.ApiResponse;
import dev.lysmux.lab4.auth.exception.AuthException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class AuthExceptionMapper implements ExceptionMapper<AuthException> {
    @Override
    public Response toResponse(AuthException exception) {
        ApiResponse<Void> response = new ApiResponse<>(exception.getMessage());

        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(response)
                .build();
    }
}
