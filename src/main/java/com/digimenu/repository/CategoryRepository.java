package com.digimenu.repository;

import com.digimenu.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    Optional<Category> findByIdAndCompanyId(UUID id, UUID companyId);

    @Query("SELECT c FROM Category c WHERE c.id IN :categoryId")
    Optional<List<Category>> findAllByIdList(List<UUID> categoryId);
}
