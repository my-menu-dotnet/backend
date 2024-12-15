package net.mymenu.service;

import net.mymenu.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    @Autowired
    private JwtHelper jwtHelper;

    public ResponseCookie createAccessTokenCookie(String jwt) {
        return createCookie("accessToken", jwt, jwtHelper.REFRESH_EXPIRY_IN_SECONDS, "/");
    }

    public ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return createCookie("refreshToken", refreshToken, jwtHelper.REFRESH_EXPIRY_IN_SECONDS, "/auth");
    }

    public ResponseCookie createCookie(String name, String value, int maxAge, String path) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(true)
                .path(path)
                .sameSite("None")
                .maxAge(maxAge)
                .build();
    }
}
