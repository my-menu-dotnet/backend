package com.digimenu.security;

import com.digimenu.models.User;
import com.digimenu.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtHelper extends RefreshTokenHelper {

    @Value("${jwt.secret}")
    private String SECRET;

    @Value("${jwt.expiration}")
    private int MINUTES;

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public User extractUser() {
        String authorizationHeaderValue = request.getHeader("Authorization");
        String token = authorizationHeaderValue.substring(7);
        Claims claims = extractAllClaims(token);
        String email = claims.getSubject();
        return userService.loadUserByEmail(email);
    }

    public String generateUserToken(User user) {
        return createToken(user);
    }

    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractEmail(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private String createToken(User user) {
        var now = Instant.now();
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(MINUTES, ChronoUnit.MINUTES)))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }
}
