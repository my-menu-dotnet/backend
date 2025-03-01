package net.mymenu.models.food_item;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import net.mymenu.models.Food;
import net.mymenu.tenant.BaseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "food_item_category")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FoodItemCategory extends BaseEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "required")
    private Boolean required = false;

    @Column(name = "min_items")
    private double minItems;

    @Column(name = "max_items")
    private double maxItems;

    @OneToMany(mappedBy = "category")
    @JsonManagedReference
    @OrderBy("order")
    private List<FoodItem> foodItems;

    @ManyToOne
    @JsonBackReference
    private Food food;

    @Column(name = "\"order\"")
    private Integer order;
}
