package net.mymenu.repository.food_item;

import net.mymenu.models.food_item.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FoodItemRepository extends JpaRepository<FoodItem, UUID> {
    void removeById(UUID id);
}
