package com.digimenu.dto.food;

import com.digimenu.dto.category.CategoryRead;
import com.digimenu.enums.FoodStatus;
import com.digimenu.models.Category;
import com.digimenu.models.FileStorage;
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
    private FileStorage image;
    private FoodStatus status;
    private CategoryRead category;
}
