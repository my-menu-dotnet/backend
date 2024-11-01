package com.digimenu.dto.food;

import com.digimenu.enums.CategoryStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class FoodCategoryResponse {
    private UUID id;
    private String name;
    private String description;
    private String image;
    private CategoryStatus status;
}
