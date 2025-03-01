package net.mymenu.models.food_item;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import net.mymenu.interfaces.Timestamped;
import net.mymenu.listeners.TimestampedListener;
import net.mymenu.models.FileStorage;
import net.mymenu.models.Food;
import net.mymenu.tenant.BaseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "food_item")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodItem extends BaseEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "desciption")
    private String description;

    @Column(name = "price_increase")
    private double priceIncrease;

    @ManyToOne
    private FileStorage image;

    @Column(name = "\"order\"")
    private Integer order;

    @ManyToOne
    @JsonBackReference
    private FoodItemCategory category;
}
