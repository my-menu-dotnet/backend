package net.mymenu.order;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.mymenu.order.enums.OrderStatus;
import net.mymenu.address.Address;
import net.mymenu.user.User;
import net.mymenu.order.order_item.OrderItem;
import net.mymenu.tenant.BaseEntity;

import java.util.List;

@Entity
@Table(name = "food_order", indexes = {
        @Index(name = "idx_order_order_number_status", columnList = "order_number, status"),
})
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Order extends BaseEntity {

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "order_number")
    @NotNull
    private Integer orderNumber;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "company_observation")
    private String companyObservation;

    @Column(name = "delivery_fee")
    private int deliveryFee;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    @Column(name = "\"order\"")
    private int order;

    @ManyToOne(cascade = CascadeType.ALL)
    private Address address;

    @ManyToOne
    private User user;

    public String getUserName() {
        return userName != null ? userName : user.getName();
    }

    public Address getAddress() {
        return address != null ? address : user.getAddress();
    }
}
