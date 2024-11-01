package com.digimenu.mapper;

import com.digimenu.dto.food.FoodCategoryResponse;
import com.digimenu.dto.food.FoodResponse;
import com.digimenu.models.Category;
import com.digimenu.models.Food;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FoodMapper {

    FoodCategoryResponse toFoodCategoryResponse(Category category);
    FoodResponse toFoodResponse(Food food);

    FoodResponse[] toFoodResponse(Food[] food);
}
