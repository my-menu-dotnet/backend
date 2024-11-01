package com.digimenu.dto.category;

import com.digimenu.enums.CategoryStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryCreate {

    @NotBlank
    private String name;

    private String description;

    private String image;

    @NotBlank
    private CategoryStatus status;
}
