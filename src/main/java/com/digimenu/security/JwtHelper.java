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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
public class JwtHelper {

    @Value("${jwt.secret}")
    private String SECRET;

    @Value("${jwt.expiration}")
    private int MINUTES;

    @Value("${jwt.temporaryExpiration}")
    private int TEMPORARY_MINUTES;

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public User extractUser(String token) {
        Claims claims = extractAllClaims(token);
        String email = claims.getSubject();
        return userService.loadUserByEmail(email);
    }

    public User extractUser() {
        String authorizationHeaderValue = request.getHeader("Authorization");
        return extractUser(authorizationHeaderValue.substring(7));
    }

    public UUID extractCompanyId(String token) {
        Claims claims = extractAllClaims(token);
        return UUID.fromString(claims.get("companyId", String.class));
    }

    public UUID extractCompanyId() {
        String authorizationHeaderValue = request.getHeader("Authorization");
        return extractCompanyId(authorizationHeaderValue.substring(7));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateTemporaryToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, user.getEmail());
    }

    public String generateToken(User user, UUID companyId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("companyId", companyId.toString());
        return createToken(claims, user.getEmail());
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
        return extractExpiration(token).before(new Date());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        var now = Instant.now();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(MINUTES, ChronoUnit.MINUTES)))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }
}
