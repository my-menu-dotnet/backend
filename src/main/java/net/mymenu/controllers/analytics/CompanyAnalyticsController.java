package net.mymenu.controllers.analytics;

import jakarta.servlet.http.HttpServletRequest;
import net.mymenu.dto.analytics.TotalCompanyAccessDTO;
import net.mymenu.models.analytics.CompanyAccess;
import net.mymenu.repository.analytics.CompanyAccessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/analytics/company")
public class CompanyAnalyticsController {

    @Autowired
    private CompanyAccessRepository companyAccessRepository;

    @Autowired
    private HttpServletRequest request;

    @PostMapping("/user-access")
    public ResponseEntity<?> save(@RequestBody CompanyAccess companyAccess) {
        String userAgent = request.getHeader("User-Agent");
        String ip = request.getRemoteAddr();

        companyAccess.setUserAgent(userAgent);
        companyAccess.setIp(ip);

        companyAccessRepository.saveAndFlush(companyAccess);

        return ResponseEntity.ok(companyAccess);
    }

    @GetMapping("/total-access")
    public ResponseEntity<?> getByCompanyId() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMonthAgo = now.minusMonths(1);

        List<CompanyAccess> accesses = companyAccessRepository.
                findAllByCreatedAtBetween(oneMonthAgo, now);
        List<CompanyAccess> previousMonthAccesses = companyAccessRepository.
                findAllByCreatedAtBetween(oneMonthAgo.minusMonths(1), oneMonthAgo);

        int totalAccessLastMonth = accesses.size();
        int totalAccessPreviousMonth = previousMonthAccesses.size();

        int totalAccessLastWeek = accesses.parallelStream()
                .filter(access -> access.getCreatedAt().isAfter(now.minusWeeks(1)))
                .toList().size();
        int totalAccessPreviousWeek = accesses.parallelStream()
                .filter(access -> access.getCreatedAt().isAfter(now.minusWeeks(2)))
                .toList().size();

        double monthGrowthPercentage = totalAccessPreviousMonth != 0
                ? (double) (totalAccessLastMonth - totalAccessPreviousMonth) / totalAccessPreviousMonth * 100
                : 0;
        double weekGrowthPercentage = totalAccessPreviousWeek != 0
                ? (double) (totalAccessLastWeek - totalAccessPreviousWeek) / totalAccessPreviousWeek * 100
                : 0;

        var monthTotal = TotalCompanyAccessDTO.builder()
                .totalAccess(totalAccessLastMonth)
                .growthPercentage(monthGrowthPercentage)
                .build();

        var weekTotal = TotalCompanyAccessDTO.builder()
                .totalAccess(totalAccessLastWeek)
                .growthPercentage(weekGrowthPercentage)
                .build();

        return ResponseEntity.ok(Map.of("month", monthTotal, "week", weekTotal));
    }
}
