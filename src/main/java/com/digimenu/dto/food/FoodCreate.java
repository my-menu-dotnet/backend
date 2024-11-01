package com.digimenu.dto.food;

import com.digimenu.enums.FoodStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class FoodCreate {

    @Valid
    @NotBlank
    String name;

    @Valid
    String description;

    @Valid
    double price;

    @Valid
    String image;

    @Valid
    @NotBlank
    FoodStatus status;

    @Valid
    @NotBlank
    @JsonProperty("category_id")
    UUID categoryId;
}
