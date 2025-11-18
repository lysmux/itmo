package dev.lysmux.lab4.auth.model;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.security.Principal;
import java.util.Set;

@Data
@Builder
public class UserPrincipal implements Principal {
    private final String id;

    @Singular
    private final Set<String> roles;

    @Override
    public String getName() {
        return id;
    }
}
