package net.mymenu.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import net.mymenu.enums.DiscountType;
import net.mymenu.enums.DiscountStatus;
import net.mymenu.interfaces.Timestamped;
import net.mymenu.listeners.TimestampedListener;
import net.mymenu.tenant.BaseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "discount")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Discount extends BaseEntity {

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    private Food food;

    @Column(name = "discount")
    private double discount;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private DiscountType type;

    @Column(name = "start_at")
    private LocalDate startAt;

    @Column(name = "end_at")
    private LocalDate endAt;

    @Column(name = "active")
    private boolean active;

    public DiscountStatus getStatus() {
        if (active) {
            if (startAt != null && startAt.isAfter(LocalDate.now())) {
                return DiscountStatus.SCHEDULED;
            } else if (endAt != null && endAt.isBefore(LocalDate.now())) {
                return DiscountStatus.EXPIRED;
            } else {
                return DiscountStatus.ACTIVE;
            }
        } else {
            return DiscountStatus.INACTIVE;
        }
    }
}
