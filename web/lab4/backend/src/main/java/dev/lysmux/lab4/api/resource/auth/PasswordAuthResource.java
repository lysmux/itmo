package dev.lysmux.lab4.api.resource.auth;

import dev.lysmux.lab4.api.ResponseUtils;
import dev.lysmux.lab4.auth.providers.password.PasswordAuthProvider;
import dev.lysmux.lab4.auth.providers.password.model.PasswordCredentials;
import dev.lysmux.lab4.auth.model.TokensPair;
import dev.lysmux.lab4.domain.model.User;
import dev.lysmux.lab4.api.schemas.auth.LoginRequest;
import dev.lysmux.lab4.api.schemas.auth.RegisterRequest;
import dev.lysmux.lab4.service.auth.AuthService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Path("/auth/password")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class PasswordAuthResource {
    @Inject
    private PasswordAuthProvider passwordAuthProvider;

    @Inject
    private AuthService authService;

    @POST
    @Path("/register")
    public Response register(@Valid RegisterRequest request) {
        User user = passwordAuthProvider.register(new PasswordCredentials(
                request.username(),
                request.password()
        ));
        TokensPair tokensPair = authService.generateTokensPair(user.id());
        return ResponseUtils.makeTokenResponse(tokensPair);
    }

    @POST
    @Path("/login")
    public Response login(@Valid LoginRequest request) {
        User user = passwordAuthProvider.login(new PasswordCredentials(
                request.username(),
                request.password()
        ));
        TokensPair tokensPair = authService.generateTokensPair(user.id());
        return ResponseUtils.makeTokenResponse(tokensPair);
    }

}
