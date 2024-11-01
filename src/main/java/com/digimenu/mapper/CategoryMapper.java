package com.digimenu.mapper;

import com.digimenu.dto.category.CategoryFoodResponse;
import com.digimenu.dto.category.CategoryResponse;
import com.digimenu.models.Category;
import com.digimenu.models.Food;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryFoodResponse toCategoryFoodResponse(Food food);
    CategoryResponse toCategoryResponse(Category category);
}
