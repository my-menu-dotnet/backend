package com.digimenu.dto.food;

import com.digimenu.dto.category.CategoryRead;
import com.digimenu.enums.FoodStatus;
import com.digimenu.models.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class FoodResponse {
    private UUID id;
    private String name;
    private String description;
    private double price;
//    private String image;
    private FoodStatus status;
    private CategoryRead category;
}
