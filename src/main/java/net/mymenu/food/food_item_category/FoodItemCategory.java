package net.mymenu.food.food_item_category;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import net.mymenu.food.Food;
import net.mymenu.food.food_item.FoodItem;
import net.mymenu.tenant.BaseEntity;

import java.util.List;

@Entity
@Table(name = "food_item_category")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FoodItemCategory extends BaseEntity {

    @Column(name = "title")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "required")
    private Boolean required = false;

    @Column(name = "min_items")
    private double minItems;

    @Column(name = "max_items")
    private double maxItems;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("category")
    @OrderBy("order")
    private List<FoodItem> foodItems;

    @ManyToOne
    @JsonBackReference
    private Food food;

    @Column(name = "\"order\"")
    private Integer order;
}
