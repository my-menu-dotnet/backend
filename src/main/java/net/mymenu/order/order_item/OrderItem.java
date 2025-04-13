package net.mymenu.order.order_item;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.mymenu.order.enums.OrderCurrency;
import net.mymenu.file_storage.FileStorage;
import net.mymenu.order.order_discount.OrderDiscount;
import net.mymenu.tenant.BaseEntity;

import java.util.List;

@Entity
@Table(name = "order_item")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderItem extends BaseEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne
    private FileStorage image;

    @Column(name = "category")
    private String category;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "observation")
    private String observation;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private OrderCurrency currency = OrderCurrency.BRL;

    @Column(name = "unit_price")
    private double unitPrice;

    @ManyToOne(cascade = CascadeType.ALL)
    private OrderDiscount discount;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    public double getTotalPrice() {
        double current = unitPrice * quantity;
        if (orderItems != null) {
            for (OrderItem orderItem : orderItems) {
                current += orderItem.getTotalPrice();
            }
        }
        return current;
    }
}
