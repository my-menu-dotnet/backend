package net.mymenu.security;

import net.mymenu.models.User;
import net.mymenu.service.AuthService;
import net.mymenu.service.CookieService;
import net.mymenu.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private CookieService cookieService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String email = null;
        String jwt = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    jwt = cookie.getValue();
                    email = jwtHelper.extractEmail(jwt);
                    break;
                }
            }
        }
        try {
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user;

                if (email.equals(jwtHelper.ANONYMOUS)) {
                    user = createGuestUser();
                } else {
                    user = userService.loadUserByEmail(email);
                }

                String uri = request.getRequestURI();

                if (validateToken(jwt, email, user, uri)) {
                    UsernamePasswordAuthenticationToken emailPasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user,
                            null,
                            user.getAuthorities());

                    emailPasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(emailPasswordAuthenticationToken);
                }
            }
            chain.doFilter(request, response);
        } catch (Exception e) {
            ResponseCookie cookieRefreshToken = cookieService.createCookie("refreshToken", "", 0, "/auth");
            ResponseCookie cookieAccessToken = cookieService.createCookie("accessToken", "", 0, "/");

            response.addHeader(HttpHeaders.SET_COOKIE, cookieRefreshToken.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, cookieAccessToken.toString());
            response.setStatus(HttpStatus.FORBIDDEN.value());
        }

    }

    private User createGuestUser() {
        return User
                .builder()
                .email(jwtHelper.ANONYMOUS)
                .isVerifiedEmail(true)
                .isActive(true)
                .build();
    }

    private boolean validateToken(String jwt, String email, User user, String uri) {
        return jwtHelper.validateToken(jwt, email) && (user.isVerifiedEmail() || uri.equals("/user/me"));
    }
}
