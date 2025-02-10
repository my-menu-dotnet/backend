package net.mymenu.dto.food.food_item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FoodItemRequest {

    private String title;

    private String description;

    @JsonProperty("price_increase")
    private double priceIncrease;

    @JsonProperty("image_id")
    private UUID imageId;
}
