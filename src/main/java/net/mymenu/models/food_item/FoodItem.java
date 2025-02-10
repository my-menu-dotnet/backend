package net.mymenu.models.food_item;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import net.mymenu.interfaces.Timestamped;
import net.mymenu.listeners.TimestampedListener;
import net.mymenu.models.FileStorage;
import net.mymenu.models.Food;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "food_item")
@EntityListeners(TimestampedListener.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodItem implements Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

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

    @Column(name = "created_at")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
