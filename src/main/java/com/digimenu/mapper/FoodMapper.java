package com.digimenu.mapper;

import com.digimenu.dto.category.CategoryRead;
import com.digimenu.dto.food.FoodResponse;
import com.digimenu.models.Category;
import com.digimenu.models.Food;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FoodMapper {

    @Mapping(target = "category", source = "category")
    FoodResponse toFood(Food food);

    CategoryRead toCategory(Category category);

    FoodResponse[] toFood(Food[] food);
}
