package com.digimenu.dto.category;

import com.digimenu.enums.CategoryStatus;
import com.digimenu.models.Company;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CategoryResponse {
    private UUID id;
    private String name;
    private String description;
    private String image;
    private CategoryStatus status;
    private List<CategoryFoodResponse> food;
}
