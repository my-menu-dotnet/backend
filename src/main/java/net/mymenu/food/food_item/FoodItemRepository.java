package net.mymenu.food.food_item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface FoodItemRepository extends JpaRepository<FoodItem, UUID> {
    void removeById(UUID id);

    @Query("SELECT fi FROM FoodItem fi WHERE fi.category.food.id = :foodId")
    Page<FoodItem> findAllByFoodId(UUID foodId, Pageable pageable);
}
