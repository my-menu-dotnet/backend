package net.mymenu.food.food_item_category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FoodItemCategoryRepository extends JpaRepository<FoodItemCategory, UUID> { }
