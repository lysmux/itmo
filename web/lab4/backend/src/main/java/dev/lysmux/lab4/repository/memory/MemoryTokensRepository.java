package dev.lysmux.lab4.repository.memory;

import dev.lysmux.lab4.auth.repository.TokensRepository;
import dev.lysmux.lab4.auth.model.RefreshToken;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Alternative
public class MemoryTokensRepository implements TokensRepository {
    private final List<RefreshToken> tokens = new ArrayList<>();

    public RefreshToken addToken(RefreshToken token) {
        tokens.add(token);
        return token;
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
