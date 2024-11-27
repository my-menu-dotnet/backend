package net.mymenu.security;

import net.mymenu.exception.NotFoundException;
import net.mymenu.models.User;
import net.mymenu.service.UserService;
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

    public final String ANONYMOUS = "anonymous";

    @Value("${jwt.secret}")
    private String SECRET;

    @Value("${jwt.expiration}")
    public int ACCESS_EXPIRY_IN_SECONDS;

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractEmail() {
        String token = extractTokenFromCookie();
        return extractEmail(token);
    }

    public User extractUser() {
        String token = extractTokenFromCookie();
        Claims claims = extractAllClaims(token);
        String email = claims.getSubject();
        return userService.loadUserByEmail(email);
    }

    public String generateUserToken(User user) {
        return createToken(user);
    }

    public String generateAnonymousToken() { return createToken(User.builder().email(ANONYMOUS).build()); }

    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractEmail(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    public Boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }

    private String extractTokenFromCookie() {
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        throw new NotFoundException("Token not found");
    }

    private String createToken(User user) {
        var now = Instant.now();
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(ACCESS_EXPIRY_IN_SECONDS, ChronoUnit.MINUTES)))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }
}
