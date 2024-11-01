package com.digimenu.dto.category;

import com.digimenu.enums.FoodStatus;

import java.util.UUID;

public class CategoryFoodResponse {
    private UUID id;
    private String name;
    private String description;
    private double price;
    private String image;
    private FoodStatus status;
}
