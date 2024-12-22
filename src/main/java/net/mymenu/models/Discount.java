package net.mymenu.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import net.mymenu.enums.SaleStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "discount")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JsonBackReference
    private Food food;

    @ManyToOne
    private Company company;

    @Column(name = "discount")
    private double discount;

    @Column(name = "start_at")
    private LocalDateTime startAt;

    @Column(name = "end_at")
    private LocalDateTime endAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SaleStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
