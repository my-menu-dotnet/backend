package net.mymenu.service;

import net.mymenu.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    @Autowired
    private JwtHelper jwtHelper;

    @Value("${cookie.domain}")
    private String cookieDomain;

    public ResponseCookie createAccessTokenCookie(String jwt) {
        return createCookie("accessToken", jwt, jwtHelper.REFRESH_EXPIRY_IN_SECONDS, "/");
    }

    // This need to be ajusted according to the path of your refresh token endpoint
    public ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return createCookie("refreshToken", refreshToken, jwtHelper.REFRESH_EXPIRY_IN_SECONDS, "/");
    }

    public ResponseCookie createIsAuthenticatedCookie(Boolean isAuthenticated) {
        System.out.println("Creating is_authenticated cookie with value: " + cookieDomain);
        return ResponseCookie.from("is_authenticated", isAuthenticated.toString())
                .httpOnly(false)
                .secure(true)
                .domain(cookieDomain)
                .sameSite("None")
                .path("/")
                .build();
    }

    public ResponseCookie createCookie(String name, String value, int maxAge, String path) {
        System.out.println("Creating createCookie cookie with value: " + cookieDomain);
        return ResponseCookie.from(name, value)
                .httpOnly(false)
                .secure(true)
                .domain(cookieDomain)
                .path(path)
                .sameSite("None")
                .maxAge(maxAge)
                .build();
    }
}
