package net.mymenu.food.food_item_category;

import net.mymenu.food.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface FoodItemCategoryRepository extends JpaRepository<FoodItemCategory, UUID> {
    List<FoodItemCategory> findAllByFoodId(UUID foodId);

    Page<FoodItemCategory> findAllByFoodId(UUID foodId, Pageable pageable);
}
