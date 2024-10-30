package com.digimenu.security.roleRequired;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RoleRequiredAspect {

    @Before("@annotation(roleRequired)")
    public void checkRole(RoleRequired roleRequired) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("User not authenticated");
        }

        boolean hasRole = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> {
                    for (String requiredRole : roleRequired.value()) {
                        if (role.equals("ROLE_" + requiredRole)) {
                            return true;
                        }
                    }
                    return false;
                });

        if (!hasRole) {
            throw new SecurityException("User does not have required role");
        }
    }
}
