package net.mymenu.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.mymenu.enums.order.OrderStatus;
import net.mymenu.interfaces.Timestamped;
import net.mymenu.listeners.TimestampedListener;
import net.mymenu.models.order.OrderItem;
import net.mymenu.tenant.BaseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "food_order")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Order extends BaseEntity {

    @Column(name = "table_number")
    private int tableNumber;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    @ManyToOne
    private User user;
}
