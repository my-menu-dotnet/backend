package net.mymenu.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    @Query("SELECT c FROM Category c WHERE c.id IN :categoryId")
    Optional<List<Category>> findAllByIdList(List<UUID> categoryId);

    List<Category> findAllByActiveTrueOrderByOrder();

    @Query("SELECT c FROM Category c ORDER BY c.order DESC LIMIT 1")
    Category findLastCategoryOrder();
}
