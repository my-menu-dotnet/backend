package com.digimenu.repository;

import com.digimenu.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findAllByCompanyId(UUID uuid);
    Optional <Category> findByIdAndCompanyId(UUID id, UUID companyId);
}
