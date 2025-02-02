package net.mymenu.repository;

import net.mymenu.models.Banner;
import net.mymenu.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BannerRepository extends JpaRepository<Banner, UUID> {
    List<Banner> findAllByCompany(Company company);
}
