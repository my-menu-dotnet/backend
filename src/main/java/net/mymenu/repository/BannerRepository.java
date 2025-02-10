package net.mymenu.repository;

import net.mymenu.models.Banner;
import net.mymenu.models.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface BannerRepository extends JpaRepository<Banner, UUID> {

    @Query("SELECT b FROM Banner b WHERE b.company = :company")
    Page<Banner> findAllByCompany(Company company, Pageable pageable);

    List<Banner> findAllByCompanyAndActiveIsTrue(Company company);

    Banner findByIdAndCompany(UUID id, Company company);

    void removeByIdAndCompany(UUID id, Company company);
}
