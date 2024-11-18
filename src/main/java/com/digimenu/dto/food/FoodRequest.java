package com.digimenu.dto.food;

import com.digimenu.constraints.FullName;
import com.digimenu.enums.FoodStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class FoodRequest {

    @FullName
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

    @Valid
    @NotBlank
    @JsonProperty("company_id")
    UUID companyId;
}
