package com.digimenu.mapper;

import com.digimenu.models.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toCategoryResponse(Category category);

    Category[] toCategory(Category[] categories);
}
