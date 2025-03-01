package net.mymenu.repository.food_item;

import net.mymenu.models.food_item.FoodItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FoodItemCategoryRepository extends JpaRepository<FoodItemCategory, UUID> { }
