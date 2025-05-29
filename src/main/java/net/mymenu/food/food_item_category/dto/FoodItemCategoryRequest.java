package net.mymenu.food.food_item_category.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import net.mymenu.constraints.EntityExists;
import net.mymenu.food.FoodRepository;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodItemCategoryRequest {

    private String name;

    private String description;

    private Boolean required = false;

    @JsonProperty("min_items")
    private double minItems;

    @JsonProperty("max_items")
    private double maxItems;

    @EntityExists(repository = FoodRepository.class)
    @NotNull
    private UUID foodId;
}
