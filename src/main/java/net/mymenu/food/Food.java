package net.mymenu.food;

import com.fasterxml.jackson.annotation.*;
import net.mymenu.category.Category;
import net.mymenu.discount.enums.DiscountStatus;
import jakarta.persistence.*;
import lombok.*;
import net.mymenu.discount.Discount;
import net.mymenu.file_storage.FileStorage;
import net.mymenu.food.food_item_category.FoodItemCategory;
import net.mymenu.tenant.BaseEntity;

import java.util.List;

@Entity
@Table(name = "food")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Food extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private double price;

    @OneToOne
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private FileStorage image;

    @Column(name = "active")
    private boolean active;

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

    @OneToMany(mappedBy = "food", cascade =  CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @JsonProperty("item_categories")
    @OrderBy("order")
    private List<FoodItemCategory> itemCategories;

    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Discount> discounts;

    @Transient
    public Discount getActiveDiscount() {
        if (this.discounts == null || this.discounts.isEmpty()) {
            return null;
        }
        return this.discounts.stream()
                .filter(discount -> discount.getStatus() == DiscountStatus.ACTIVE)
                .findFirst()
                .orElse(null);
    }
}
