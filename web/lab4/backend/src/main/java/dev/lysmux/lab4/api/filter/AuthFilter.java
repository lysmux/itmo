package dev.lysmux.lab4.api.filter;

import dev.lysmux.lab4.AppSecurityContext;
import dev.lysmux.lab4.AuthException;
import dev.lysmux.lab4.service.AuthService;
import dev.lysmux.lab4.service.UserPrincipal;
import io.jsonwebtoken.JwtException;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION)
@Slf4j
public class AuthFilter implements ContainerRequestFilter {
    @Inject
    private AuthService authService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        UserPrincipal user = null;

        Cookie authCookie = requestContext.getCookies().get("accessToken");
        if (authCookie != null) {
            try {
                user = validateToken(authCookie.getValue());
            } catch (AuthException e) {
                log.warn("Invalid JWT token", e);
            }
        }

        boolean isSecure = requestContext.getSecurityContext().isSecure();
        SecurityContext securityContext = new AppSecurityContext(user, "JWT", isSecure);
        requestContext.setSecurityContext(securityContext);
    }

    private UserPrincipal validateToken(String authHeader) {
        return authService.validateAccessToken(authHeader);
    }
}
