package net.mymenu.repository.analytics;

import net.mymenu.models.analytics.CompanyAccess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface CompanyAccessRepository extends JpaRepository<CompanyAccess, Integer> {
    List<CompanyAccess> findAllByCreatedAtBetween(LocalDateTime createdAtAfter, LocalDateTime createdAtBefore);
}
