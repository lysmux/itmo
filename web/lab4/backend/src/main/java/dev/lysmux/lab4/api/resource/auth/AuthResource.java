package dev.lysmux.lab4.api.resource.auth;

import dev.lysmux.lab4.api.ResponseUtils;
import dev.lysmux.lab4.auth.model.TokensPair;
import dev.lysmux.lab4.service.auth.AuthService;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class AuthResource {
    @Inject
    private AuthService authService;

    @POST
    @Path("/logout")
    public Response logout(@NotNull @CookieParam("refreshToken") String refreshToken) {
        authService.banToken(refreshToken);
        return ResponseUtils.makeClearTokenResponse();
    }

    @POST
    @Path("/refresh")
    public Response refreshTokens(@NotNull @CookieParam("refreshToken") String refreshToken) {
        TokensPair tokensPair = authService.refreshTokensPair(refreshToken);
        return ResponseUtils.makeTokenResponse(tokensPair);
    }
}
