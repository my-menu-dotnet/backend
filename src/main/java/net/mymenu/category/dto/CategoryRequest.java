package net.mymenu.category.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {

    @NotBlank
    @NotNull
    private String name;

    @NotBlank
    @NotNull
    private boolean active;
}
