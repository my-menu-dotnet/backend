package com.digimenu.dto.category;

import com.digimenu.enums.CategoryStatus;
import com.digimenu.models.FileStorage;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CategoryRead {
    private UUID id;
    private String name;
    private String description;
    private FileStorage image;
    private CategoryStatus status;
}
