package dev.lysmux.lab4.repository;

import dev.lysmux.lab4.domain.RefreshToken;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TokensRepository {
    private final List<RefreshToken> tokens = new ArrayList<>();

    public void addToken(RefreshToken token) {
        tokens.add(token);
    }

    public void banToken(String tokenId) {
        RefreshToken token = getTokenById(tokenId);
        if (token != null) {
            tokens.remove(token);
            tokens.add(new RefreshToken(token.id(), true));
        }
    }

    public RefreshToken getTokenById(String tokenId) {
        return tokens.stream()
                .filter(token -> token.id().equals(tokenId))
                .findFirst()
                .orElse(null);
    }
}
