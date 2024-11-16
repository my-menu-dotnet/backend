package com.digimenu.dto.category;

import com.digimenu.enums.CategoryStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CategoryCreate {

    @NotBlank
    private String name;

    private String description;

    private UUID imageId;

    @NotBlank
    private CategoryStatus status;

    private UUID companyId;
}
