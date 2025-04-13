package net.mymenu.tenant;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.mymenu.exception.AccountHasNoCompany;
import net.mymenu.exception.NotFoundException;
import net.mymenu.company.Company;
import net.mymenu.user.User;
import net.mymenu.company.CompanyRepository;
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

    @Autowired
    private CompanyRepository companyRepository;

    private static final List<String> FULL_ACCESS_URLS = List.of(
            "/company",
            "/user",
            "/v1/oauth/refresh-token",
            "/v1/oauth/logout",
            "/v1/oauth/google",
            "/file/upload",
            "/address"
    );

    @Override
    public boolean preHandle(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull Object handler) {
        String uri = request.getRequestURI();
        String method = request.getMethod();

        UUID tenantId;

        if (uri.startsWith("/menu") || (uri.startsWith("/order") && method.equals("POST")) || uri.equals("/order/user") || uri.equals("/order/user/total")) {
            String companyUrl = request.getHeader("X-Company-ID");

            System.out.println("TENATN BY HEADER " + companyUrl);

            Optional<Company> company = companyRepository.findByUrl(companyUrl);

            tenantId = company.map(Company::getId)
                    .orElseThrow(() -> new NotFoundException("Company not found"));
        } else {
            if (!jwtHelper.isAuthenticated() || FULL_ACCESS_URLS.contains(uri)) {
                return true;
            }

            User user = jwtHelper.extractUser();
            tenantId = Optional.ofNullable(user.getCompany())
                    .map(Company::getId)
                    .orElseThrow(() -> new AccountHasNoCompany("Account has no company"));

        }

        System.out.println("tenantId = " + tenantId);

        if (tenantId == null) {
            throw new AccountHasNoCompany("Account has no company");
        }

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
