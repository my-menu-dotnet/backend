package net.mymenu.food.food_item;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import net.mymenu.file_storage.FileStorage;
import net.mymenu.food.food_item_category.FoodItemCategory;
import net.mymenu.tenant.BaseEntity;

@Entity
@Table(name = "food_item")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodItem extends BaseEntity {

    @Column(name = "title")
    private String name;

    @Column(name = "desciption")
    private String description;

    @Column(name = "price_increase")
    private double priceIncrease;

    @ManyToOne
    private FileStorage image;

    @Column(name = "\"order\"")
    private Integer order;

    @ManyToOne
    @JsonIgnoreProperties("foodItems")
    private FoodItemCategory category;
}
