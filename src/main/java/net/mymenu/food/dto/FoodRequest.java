package net.mymenu.food.dto;

import net.mymenu.food.enums.FoodStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class FoodRequest {

    String name;

    @Valid
    String description;

    @Valid
    double price;

    UUID imageId;

    @Valid
    @NotBlank
    FoodStatus status;

    @Valid
    @NotBlank
    @JsonProperty("category_id")
    UUID categoryId;

    @JsonProperty("lactose_free")
    boolean lactoseFree;

    @JsonProperty("gluten_free")
    boolean glutenFree;

    boolean vegan;

    boolean vegetarian;

    boolean halal;
}
