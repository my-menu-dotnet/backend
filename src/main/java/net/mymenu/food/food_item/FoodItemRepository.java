package net.mymenu.food.food_item;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FoodItemRepository extends JpaRepository<FoodItem, UUID> {
    void removeById(UUID id);
}
