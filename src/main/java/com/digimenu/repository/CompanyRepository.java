package com.digimenu.repository;

import com.digimenu.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {

    @Query("SELECT c FROM Company c JOIN c.users u WHERE u.id = :userId")
    Optional<List<Company>> findAllByUser(UUID userId);
}
