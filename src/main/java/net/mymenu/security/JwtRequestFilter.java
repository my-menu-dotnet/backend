package net.mymenu.security;

import net.mymenu.models.User;
import net.mymenu.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (email.equals(jwtHelper.ANONYMOUS)) {
                User anonymous = User.builder().email(jwtHelper.ANONYMOUS).build();
                UsernamePasswordAuthenticationToken anonymousAuthenticationToken = new UsernamePasswordAuthenticationToken(anonymous,
                        null,
                        anonymous.getAuthorities());

                anonymousAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(anonymousAuthenticationToken);
            } else {
                User user = userService.loadUserByEmail(email);

                if (jwtHelper.validateToken(jwt, email)) {

                    UsernamePasswordAuthenticationToken emailPasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user,
                            null,
                            user.getAuthorities());

                    emailPasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(emailPasswordAuthenticationToken);
                }
            }
        }
        chain.doFilter(request, response);
    }
}
