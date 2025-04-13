package net.mymenu.food.food_item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodItemCategoryRequest {

    private String title;

    private String description;

    private Boolean required = false;

    @JsonProperty("min_items")
    private double minItems;

    @JsonProperty("max_items")
    private double maxItems;
}
