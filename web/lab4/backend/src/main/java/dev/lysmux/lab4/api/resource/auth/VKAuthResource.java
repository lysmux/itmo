package dev.lysmux.lab4.api.resource.auth;

import dev.lysmux.lab4.api.ResponseUtils;
import dev.lysmux.lab4.api.schemas.auth.VKCallbackRequest;
import dev.lysmux.lab4.auth.model.TokensPair;
import dev.lysmux.lab4.auth.providers.vk.VKAuthProvider;
import dev.lysmux.lab4.auth.providers.vk.model.VKCredentials;
import dev.lysmux.lab4.domain.model.User;
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

@Path("/auth/callback/vk")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class VKAuthResource {
    @Inject
    private VKAuthProvider vkAuthProvider;

    @Inject
    private AuthService authService;

    @POST
    @Path("")
    public Response vkCallback(@Valid VKCallbackRequest request) {
        User user = vkAuthProvider.register(new VKCredentials(
                request.code(),
                request.deviceId(),
                request.challengeVerifier()
        ));
        TokensPair tokensPair = authService.generateTokensPair(user.id());
        return ResponseUtils.makeTokenResponse(tokensPair);
    }
}
