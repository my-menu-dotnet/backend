package net.mymenu.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "food_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "table_number")
    private int tableNumber;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "status")
    private String status;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Food> foods;

    @Column(name = "company_id")
    private UUID companyId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
