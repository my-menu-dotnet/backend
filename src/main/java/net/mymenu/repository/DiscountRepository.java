package net.mymenu.repository;

import net.mymenu.models.Company;
import net.mymenu.models.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DiscountRepository extends JpaRepository<Discount, UUID> {
    Optional<List<Discount>> findAllByCompany(Company company);
}
