package net.mymenu.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import net.mymenu.enums.DiscountType;
import net.mymenu.enums.DiscountStatus;
import net.mymenu.interfaces.Timestamped;
import net.mymenu.listeners.TimestampedListener;

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
@EntityListeners(TimestampedListener.class)
public class Discount implements Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    private Food food;

    @ManyToOne
    @JsonIgnore
    private Company company;

    @Column(name = "discount")
    private double discount;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private DiscountType type;

    @Column(name = "start_at")
    private LocalDate startAt;

    @Column(name = "end_at")
    private LocalDate endAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private DiscountStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public DiscountStatus getStatus() {
        if (this.status == DiscountStatus.INACTIVE) {
            return DiscountStatus.INACTIVE;
        }
        if (this.endAt != null && this.endAt.isBefore(LocalDate.now())) {
            return DiscountStatus.EXPIRED;
        }
        if (this.startAt != null && this.startAt.isAfter(LocalDate.now())) {
            return DiscountStatus.PENDING;
        }
        return status;
    }
}
