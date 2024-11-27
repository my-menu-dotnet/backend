package net.mymenu.dto.menu;

import net.mymenu.enums.CategoryStatus;
import net.mymenu.models.FileStorage;
import net.mymenu.models.Food;
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
