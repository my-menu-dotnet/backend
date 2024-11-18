package com.digimenu.dto.menu;

import com.digimenu.enums.CategoryStatus;
import com.digimenu.models.FileStorage;
import com.digimenu.models.Food;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class MenuCategoryDTO {
    private UUID id;

    private String name;

    private String description;

    private FileStorage image;

    private CategoryStatus status;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    private List<Food> food;
}
