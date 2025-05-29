package net.mymenu.category.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.mymenu.food.Food;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryResponse {

    private UUID id;

    private Integer order;

    private String name;

    @JsonIgnoreProperties("category")
    private List<Food> foods;

    private boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
