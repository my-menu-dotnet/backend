package net.mymenu.dto.category;

import net.mymenu.enums.CategoryStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CategoryRequest {

    @NotBlank
    private String name;

    private String description;

    @JsonProperty("image_id")
    @Nullable
    private UUID imageId;

    @NotBlank
    private CategoryStatus status;
}
