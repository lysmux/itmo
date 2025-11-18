package dev.lysmux.lab4.api.resource.auth;


import dev.lysmux.lab4.api.ResponseUtils;
import dev.lysmux.lab4.api.filter.Secured;
import dev.lysmux.lab4.api.schemas.ApiResponse;
import dev.lysmux.lab4.auth.model.TokensPair;
import dev.lysmux.lab4.auth.providers.passkey.PassKeyAuthProvider;
import dev.lysmux.lab4.auth.providers.passkey.model.PassKeyLoginFinishRequest;
import dev.lysmux.lab4.service.auth.AuthService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import lombok.extern.slf4j.Slf4j;

@Path("/auth/passkey")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class PassKeyAuthResource {
    @Inject
    private AuthService authService;

    @Inject
    private PassKeyAuthProvider passKeyAuthProvider;

    @GET
    @Path("/register")
    @Secured
    public Response passkeyRegisterStart(@Context SecurityContext securityContext) {
        String options = passKeyAuthProvider.registerStart(securityContext.getUserPrincipal().getName());
        return Response.ok(options).build();
    }

    @POST
    @Path("/register")
    @Secured
    public Response passkeyRegisterFinish(@Context SecurityContext securityContext, String request) throws Exception {
        passKeyAuthProvider.registerFinish(securityContext.getUserPrincipal().getName(), request);
        return Response.ok(new ApiResponse<>("PassKey added")).build();
    }

    @GET
    @Path("/login")
    public Response passkeyLoginStart() {
        return Response.ok(passKeyAuthProvider.loginStart()).build();
    }


    @POST
    @Path("/login")
    public Response passkeyLoginFinish(PassKeyLoginFinishRequest request) {
        String userId = passKeyAuthProvider.loginFinish(request);

        TokensPair tokensPair = authService.generateTokensPair(userId);
        return ResponseUtils.makeTokenResponse(tokensPair);
    }
}
