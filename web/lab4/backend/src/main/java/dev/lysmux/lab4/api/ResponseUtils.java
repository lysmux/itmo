package dev.lysmux.lab4.api;

import dev.lysmux.lab4.auth.model.TokensPair;
import dev.lysmux.lab4.api.schemas.auth.AuthResponse;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

public class ResponseUtils {
    public static Response makeTokenResponse(TokensPair tokensPair) {
        AuthResponse response = new AuthResponse(tokensPair.accessToken().expiresIn());

        NewCookie refreshTokenCookie = new NewCookie.Builder("refreshToken")
                .value(tokensPair.refreshToken().token())
                .maxAge(tokensPair.refreshToken().expiresIn())
                .path("/")
                .secure(true)
                .httpOnly(true)
                .sameSite(NewCookie.SameSite.LAX)
                .build();

        NewCookie accessTokenCookie = new NewCookie.Builder("accessToken")
                .value(tokensPair.accessToken().token())
                .maxAge(tokensPair.accessToken().expiresIn())
                .path("/")
                .secure(true)
                .httpOnly(true)
                .sameSite(NewCookie.SameSite.LAX)
                .build();

        return Response
                .ok(response)
                .cookie(accessTokenCookie, refreshTokenCookie)
                .build();
    }

    public static Response makeClearTokenResponse() {
        NewCookie refreshTokenCookie = new NewCookie.Builder("refreshToken")
                .maxAge(0)
                .path("/")
                .secure(true)
                .httpOnly(true)
                .sameSite(NewCookie.SameSite.LAX)
                .build();

        NewCookie accessTokenCookie = new NewCookie.Builder("accessToken")
                .maxAge(0)
                .path("/")
                .secure(true)
                .httpOnly(true)
                .sameSite(NewCookie.SameSite.LAX)
                .build();

        return Response
                .ok()
                .cookie(accessTokenCookie, refreshTokenCookie)
                .build();
    }
}
