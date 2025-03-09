package net.mymenu.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.mymenu.enums.CategoryStatus;
import jakarta.persistence.*;
import lombok.*;
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

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CategoryStatus status;

    @PrePersist
    public void prePersist() {
        if (this.order == null) {
            this.order = 0;
        }
    }
}
