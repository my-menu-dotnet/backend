package net.mymenu.security;

import io.jsonwebtoken.ExpiredJwtException;
import net.mymenu.exception.TokenExpiredException;
import net.mymenu.models.User;
import net.mymenu.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain chain) {
        String uri = request.getRequestURI();

        // Se é uma rota OAuth, pular completamente o processamento do JWT
        if (uri.startsWith("/v1/oauth")) {
            try {
                chain.doFilter(request, response);
            } catch (Exception e) {
                logger.error("Error in OAuth route", e);
                resolver.resolveException(request, response, null, e);
            }
            return;
        }

        String email = null;
        String jwt = null;

        try {
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if ("accessToken".equals(cookie.getName())) {
                        jwt = cookie.getValue();
                        if (jwt != null && !jwt.isEmpty()) {
                            email = jwtHelper.extractEmail(jwt);
                            break;
                        }
                    }
                }
            }

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userService.loadUserByEmail(email);

                validateExpiredToken(jwt, email);

                UsernamePasswordAuthenticationToken emailPasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user,
                        null,
                        user.getAuthorities());

                emailPasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(emailPasswordAuthenticationToken);
            }
            chain.doFilter(request, response);
        } catch (ExpiredJwtException | TokenExpiredException e) {
            resolver.resolveException(request, response, null, new TokenExpiredException("Token expired"));
        } catch (Exception e) {
            logger.error("Error", e);
            resolver.resolveException(request, response, null, e);
        }
    }

    private void validateExpiredToken(String jwt, String email) {
        if (!jwtHelper.validateToken(jwt, email)) {
            throw new TokenExpiredException("Token expired");
        }
    }
}
