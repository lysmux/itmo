package dev.lysmux.lab4.api.resource;

import dev.lysmux.lab4.VKAuthProvider;
import dev.lysmux.lab4.domain.TokensPair;
import dev.lysmux.lab4.schemas.auth.AuthResponse;
import dev.lysmux.lab4.schemas.auth.LoginRequest;
import dev.lysmux.lab4.schemas.auth.RegisterRequest;
import dev.lysmux.lab4.schemas.auth.VKCallbackRequest;
import dev.lysmux.lab4.service.AuthService;
import dev.lysmux.lab4.service.UserService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class AuthResource {
    @Inject
    private UserService userService;

    @Inject
    private AuthService authService;

    @Inject
    private VKAuthProvider vkAuthProvider;

    @POST
    @Path("/register")
    public Response register(@Valid RegisterRequest request) {
        TokensPair tokensPair = userService.register(request.username(), request.password());
        return makeTokenResponse(tokensPair);
    }

    @POST
    @Path("/login")
    public Response login(@Valid LoginRequest request) {
        TokensPair tokensPair = userService.login(request.username(), request.password());
        return makeTokenResponse(tokensPair);
    }

    @POST
    @Path("/logout")
    public Response logout(@NotNull @CookieParam("refreshToken") String refreshToken) {
        authService.banToken(refreshToken);
        return Response.noContent().build();
    }

    @POST
    @Path("/refresh")
    public Response refreshTokens(@NotNull @CookieParam("refreshToken") String refreshToken) {
        TokensPair tokensPair = authService.refreshTokensPair(refreshToken);
        return makeTokenResponse(tokensPair);
    }

    @POST
    @Path("/callback/vk")
    public Response vkCallback(@Valid VKCallbackRequest request) {
        VKAuthProvider.VKUser vkUser = vkAuthProvider.exchangeCode(request.code(), request.deviceId(), request.challengeVerifier());

        TokensPair tokensPair;
        if (userService.isUserExists(vkUser.username())) {
            tokensPair = userService.login(vkUser.username(), null);
        } else {
            tokensPair = userService.register(vkUser.username(), null);
        }

        return makeTokenResponse(tokensPair);
    }

    private Response makeTokenResponse(TokensPair tokensPair) {
        AuthResponse response = new AuthResponse(tokensPair.accessToken().expiresIn());

        NewCookie refreshTokenCookie = new NewCookie.Builder("refreshToken")
                .value(tokensPair.refreshToken().token())
                .maxAge(tokensPair.refreshToken().expiresIn())
                .path("/")
                .secure(true)
                .httpOnly(true)
                .sameSite(NewCookie.SameSite.NONE)
                .build();

        NewCookie accessTokenCookie = new NewCookie.Builder("accessToken")
                .value(tokensPair.accessToken().token())
                .maxAge(tokensPair.accessToken().expiresIn())
                .path("/")
                .secure(true)
                .httpOnly(true)
                .sameSite(NewCookie.SameSite.NONE)
                .build();

        return Response
                .ok(response)
                .cookie(accessTokenCookie, refreshTokenCookie)
                .build();
    }
}
