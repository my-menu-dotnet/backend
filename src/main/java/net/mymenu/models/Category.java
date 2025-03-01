package net.mymenu.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.mymenu.enums.CategoryStatus;
import net.mymenu.interfaces.Timestamped;
import net.mymenu.listeners.TimestampedListener;
import jakarta.persistence.*;
import lombok.*;
import net.mymenu.tenant.BaseEntity;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "category")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category extends BaseEntity {

    @Column(name = "\"order\"")
    @ColumnDefault("0")
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
