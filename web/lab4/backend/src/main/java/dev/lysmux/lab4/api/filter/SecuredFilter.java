package dev.lysmux.lab4.api.filter;

import dev.lysmux.lab4.auth.exception.AuthException;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider
@Secured
@Priority(Priorities.AUTHENTICATION + 1)
public class SecuredFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        SecurityContext securityContext = requestContext.getSecurityContext();
        if (securityContext.getUserPrincipal() == null) {
            throw new AuthException("Not authenticated");
        }
    }
}
