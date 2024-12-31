package net.mymenu.models;

import com.fasterxml.jackson.annotation.*;
import net.mymenu.enums.DiscountStatus;
import net.mymenu.enums.FoodStatus;
import net.mymenu.interfaces.Timestamped;
import net.mymenu.listeners.TimestampedListener;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "food")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners({TimestampedListener.class})
@Builder
public class Food implements Timestamped {

    @Id
    @Column(name = "id", unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private double price;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private FileStorage image;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private FoodStatus status;

    @Column(name = "lactose_free")
    private boolean lactoseFree;

    @Column(name = "gluten_free")
    private boolean glutenFree;

    @Column(name = "vegan")
    private boolean vegan;

    @Column(name = "vegetarian")
    private boolean vegetarian;

    @Column(name = "halal")
    private boolean halal;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonIgnoreProperties("foods")
    private Category category;

    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Discount> discounts;

    @Transient
    private Discount activeDiscount;

    @ManyToOne
    @JoinColumn(name = "company_id")
    @JsonIgnore
    private Company company;

    @Column(name = "created_at")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    public Discount getActiveDiscount() {
        if (this.discounts == null || this.discounts.isEmpty()) {
            return null;
        }
        return this.discounts.stream()
                .filter(discount -> discount.getStatus() == null || discount.getStatus() == DiscountStatus.ACTIVE)
                .findFirst()
                .orElse(null);
    }
}
