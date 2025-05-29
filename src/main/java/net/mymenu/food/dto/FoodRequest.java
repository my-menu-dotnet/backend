package net.mymenu.food.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.mymenu.category.CategoryRepository;
import net.mymenu.constraints.EntityExists;
import net.mymenu.file_storage.FileStorageRepository;
import net.mymenu.food.FoodRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FoodRequest {

    @NotNull
    @NotBlank
    private String name;

    private String description;

    @NotNull
    private Double price;

    @EntityExists(repository = FileStorageRepository.class)
    private UUID imageId;

    @NotNull
    private boolean active;

    @NotNull
    @JsonProperty("category_id")
    @EntityExists(repository = CategoryRepository.class)
    private UUID categoryId;

    @JsonProperty("lactose_free")
    private boolean lactoseFree;

    @JsonProperty("gluten_free")
    private boolean glutenFree;

    private boolean vegan;

    private boolean vegetarian;

    private boolean halal;
}
