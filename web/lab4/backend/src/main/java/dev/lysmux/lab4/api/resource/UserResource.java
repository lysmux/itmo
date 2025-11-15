package dev.lysmux.lab4.api.resource;

import dev.lysmux.lab4.api.filter.Secured;
import dev.lysmux.lab4.domain.User;
import dev.lysmux.lab4.service.UserService;
import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import lombok.extern.slf4j.Slf4j;

@Path( "/users")
@Secured
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class UserResource {
    @Context
    private SecurityContext securityContext;

    @Inject
    private UserService userService;

    @Path("/me")
    @GET
    public User me() {
        return userService.getUser(securityContext.getUserPrincipal().getName());
    }
}
