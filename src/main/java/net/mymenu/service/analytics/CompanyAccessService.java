package net.mymenu.service.analytics;

import jakarta.servlet.http.HttpServletRequest;
import net.mymenu.enums.analytics.AccessWays;
import net.mymenu.models.analytics.CompanyAccess;
import net.mymenu.repository.analytics.CompanyAccessRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CompanyAccessService {

    private static final Logger logger = LoggerFactory.getLogger(CompanyAccessService.class);

    @Autowired
    private CompanyAccessRepository companyAccessRepository;

    @Autowired
    private HttpServletRequest request;

    public void logCompanyAccess(AccessWays ways, UUID companyId) {
        String userAgent = request.getHeader("User-Agent");
        String ip = request.getRemoteAddr();

        CompanyAccess companyAccess = CompanyAccess
                .builder()
                .accessWay(ways)
                .accessedAt(LocalDateTime.now())
                .userAgent(userAgent)
                .ip(ip)
                .companyId(companyId)
                .build();

        logger.info("Saving CompanyAccess: {}", companyAccess);

        companyAccessRepository.save(companyAccess);

        logger.info("CompanyAccess saved successfully");
    }
}
