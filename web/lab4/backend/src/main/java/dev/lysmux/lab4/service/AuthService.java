package dev.lysmux.lab4.service;

import com.fasterxml.uuid.Generators;
import dev.lysmux.lab4.AuthException;
import dev.lysmux.lab4.domain.RefreshToken;
import dev.lysmux.lab4.domain.Token;
import dev.lysmux.lab4.domain.TokensPair;
import dev.lysmux.lab4.repository.TokensRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@ApplicationScoped
public class AuthService {
    @Inject
    private TokensRepository tokensRepository;

    private final static SecretKey SECRET = Keys.hmacShaKeyFor("MySuperSecretKey32BytesExactly!!".getBytes(StandardCharsets.UTF_8));

    private final JwtParser jwtParser = Jwts.parser()
            .verifyWith(SECRET)
            .build();

    private final static int ACCESS_TOKEN_EXPIRATION_MS = 1000 * 60 * 5; // 5 min
    private final static int REFRESH_TOKEN_EXPIRATION_MS = 1000 * 60 * 60 * 24 * 7; // 7 days

    public TokensPair generateTokensPair(String userId) {
        String accessToken = generateAccessToken(userId);
        String refreshToken = generateRefreshToken(userId);

        return new TokensPair(
                new Token(accessToken, ACCESS_TOKEN_EXPIRATION_MS),
                new Token(refreshToken, REFRESH_TOKEN_EXPIRATION_MS)
        );
    }

    public UserPrincipal validateAccessToken(String token) {
        try {
            Claims claims = jwtParser.parseSignedClaims(token).getPayload();
            return UserPrincipal.builder()
                    .id(claims.getSubject())
                    .build();
        } catch (JwtException e) {
            throw new AuthException("Could not decode token");
        }
    }

    public TokensPair refreshTokensPair(String token) {
        Claims claims;
        try {
            claims = jwtParser.parseSignedClaims(token).getPayload();
        } catch (JwtException e) {
            throw new AuthException("Could not decode token");
        }

        String tokenId = claims.getSubject();
        String userId = claims.get("userId", String.class);

        RefreshToken refreshToken = tokensRepository.getTokenById(tokenId);
        if (refreshToken != null && !refreshToken.banned()) {
            tokensRepository.banToken(tokenId);
            return generateTokensPair(userId);
        }

        throw new AuthException("Invalid refresh token");
    }

    public void banToken(String token) {
        Claims claims;
        try {
            claims = jwtParser.parseSignedClaims(token).getPayload();
        } catch (JwtException e) {
            return;
        }

        String tokenId = claims.getSubject();
        tokensRepository.banToken(tokenId);
    }

    private String generateAccessToken(String userId) {
        return generateToken(userId, Map.of(), ACCESS_TOKEN_EXPIRATION_MS);
    }

    private String generateRefreshToken(String userId) {
        String refreshTokenId = Generators.timeBasedEpochRandomGenerator().generate().toString();
        tokensRepository.addToken(new RefreshToken(refreshTokenId, false));

        return generateToken(refreshTokenId, Map.of("userId", userId), REFRESH_TOKEN_EXPIRATION_MS);
    }

    private String generateToken(String subject, Map<String, Object> claims, long expirationMs) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(SECRET)
                .compact();
    }
}
