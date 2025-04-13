package net.mymenu.order.order_discount;

import jakarta.persistence.*;
import lombok.*;
import net.mymenu.discount.enums.DiscountType;
import net.mymenu.tenant.BaseEntity;

import java.time.LocalDate;

@Entity
@Table(name = "order_discount")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDiscount extends BaseEntity {

    @Column(name = "discount")
    private double discount;

    @Column(name = "type")
    private DiscountType type;

    @Column(name = "start_at")
    private LocalDate startAt;

    @Column(name = "end_at")
    private LocalDate endAt;
}
