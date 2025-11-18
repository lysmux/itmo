package dev.lysmux.lab4.auth.repository;

import dev.lysmux.lab4.auth.model.RefreshToken;

public interface TokensRepository {
    RefreshToken addToken(RefreshToken token);

    void banToken(String tokenId);

    RefreshToken getTokenById(String tokenId);
}
