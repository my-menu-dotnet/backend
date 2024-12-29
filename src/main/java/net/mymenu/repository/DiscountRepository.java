package net.mymenu.repository;

import net.mymenu.models.Company;
import net.mymenu.models.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DiscountRepository extends JpaRepository<Discount, UUID> {

    Optional<Discount> findByIdAndCompany(UUID id, Company company);

    @Query("SELECT d FROM Discount d WHERE d.company = :company ORDER BY " +
            "CASE d.status " +
            "WHEN net.mymenu.enums.DiscountStatus.ACTIVE THEN 1 " +
            "WHEN net.mymenu.enums.DiscountStatus.PENDING THEN 2 " +
            "WHEN net.mymenu.enums.DiscountStatus.INACTIVE THEN 3 " +
            "WHEN net.mymenu.enums.DiscountStatus.EXPIRED THEN 4 " +
            "END")
    Optional<List<Discount>> findAllByCompanyOrderByStatus(Company company);

}
