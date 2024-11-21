package com.digimenu.security;

import com.digimenu.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public abstract class RefreshTokenHelper {

    @Value("${jwt.refresh.expiration}")
    public int REFRESH_EXPIRY_IN_SECONDS;

    @Value("${jwt.refresh.secret}")
    private String REFRESH_SECRET;

    public String extractRefreshTokenEmail(String token) {
        return Jwts.parser()
                .setSigningKey(REFRESH_SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String generateRefreshToken(User user) {
        return createRefreshToken(user);
    }

    private String createRefreshToken(User user) {
        var now = Instant.now();
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(REFRESH_EXPIRY_IN_SECONDS, ChronoUnit.DAYS)))
                .signWith(SignatureAlgorithm.HS256, REFRESH_SECRET)
                .compact();
    }
}
