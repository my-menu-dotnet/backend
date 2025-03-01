package net.mymenu.tenant;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.mymenu.exception.AccountHasNoCompany;
import net.mymenu.models.Company;
import net.mymenu.models.User;
import net.mymenu.security.JwtHelper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class TenantInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtHelper jwtHelper;

    private static final List<String> PUBLIC_URLS = List.of(
            "/auth/login",
            "/auth/register",
            "/auth/refresh-token",
            "/auth/logout",
            "/company",
            "/user",
            "/file/upload"
    );

    @Override
    public boolean preHandle(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull Object handler) {
        String uri = request.getRequestURI();

        if (!jwtHelper.isAuthenticated() || PUBLIC_URLS.contains(uri)) {
            return true;
        }

        User user = jwtHelper.extractUser();
        UUID tenantId = Optional.ofNullable(user.getCompany())
                .map(Company::getId)
                .orElseThrow(() -> new AccountHasNoCompany("Account has no company"));

        TenantContext.setCurrentTenant(tenantId);

        return true;
    }

    @Override
    public void afterCompletion(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull Object handler,
            Exception ex) {
        TenantContext.clear();
    }
}
