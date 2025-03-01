package net.mymenu.repository;

import net.mymenu.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
    Optional<Company> findByIdOrUrl(UUID id, String url);

    void removeById(UUID id);
}
