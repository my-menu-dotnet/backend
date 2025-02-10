package net.mymenu.repository.food_item;

import net.mymenu.models.Company;
import net.mymenu.models.food_item.FoodItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FoodItemCategoryRepository extends JpaRepository<FoodItemCategory, UUID> {
    List<FoodItemCategory> findAllByFood_Company(Company foodCompany);
}
