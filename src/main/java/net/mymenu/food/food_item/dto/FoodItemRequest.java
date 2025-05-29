package net.mymenu.food.food_item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.mymenu.constraints.EntityExists;
import net.mymenu.file_storage.FileStorageRepository;
import net.mymenu.food.food_item.FoodItemRepository;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FoodItemRequest {

    private String name;

    private String description;

    @JsonProperty("price_increase")
    private double priceIncrease;

    @JsonProperty("image_id")
    @EntityExists(repository = FileStorageRepository.class)
    private UUID imageId;

    @EntityExists(repository = FoodItemRepository.class)
    @NotNull
    private UUID categoryId;
}
