package dev.lysmux.lab4.api;

import dev.lysmux.lab4.auth.model.UserPrincipal;
import jakarta.ws.rs.core.SecurityContext;
import lombok.AllArgsConstructor;

import java.security.Principal;

@AllArgsConstructor
public class AppSecurityContext implements SecurityContext {
    private final UserPrincipal user;
    private final String authScheme;
    private final boolean isSecure;

    @Override
    public Principal getUserPrincipal() {
        return user;
    }

    @Override
    public boolean isUserInRole(String role) {
        if (user == null) {
            return false;
        }

        return user.getRoles().contains(role);
    }

    @Override
    public boolean isSecure() {
        return isSecure;
    }

    @Override
    public String getAuthenticationScheme() {
        return authScheme;
    }
}
