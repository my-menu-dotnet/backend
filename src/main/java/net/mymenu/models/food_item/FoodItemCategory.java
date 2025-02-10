package net.mymenu.models.food_item;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import net.mymenu.interfaces.Timestamped;
import net.mymenu.listeners.TimestampedListener;
import net.mymenu.models.Food;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "food_item_category")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(TimestampedListener.class)
@Builder
public class FoodItemCategory implements Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

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

    @Column(name = "created_at")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
