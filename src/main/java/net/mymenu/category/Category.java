package net.mymenu.category;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import net.mymenu.food.Food;
import net.mymenu.tenant.BaseEntity;

import java.util.List;

@Entity
@Table(name = "category")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category extends BaseEntity {

    @Column(name = "\"order\"")
    private Integer order;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("category")
    @OrderBy("status ASC")
    private List<Food> foods;

    @Column(name = "active")
    private boolean active;

    @PrePersist
    public void prePersist() {
        if (this.order == null) {
            this.order = 0;
        }
    }
}
