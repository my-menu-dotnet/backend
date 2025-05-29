package net.mymenu.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import net.mymenu.constraints.EntityExists;
import net.mymenu.discount.enums.DiscountType;
import net.mymenu.food.FoodRepository;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class OrderItemRequest {

    @EntityExists(repository = FoodRepository.class)
    @Nullable
    private UUID foodId;

    @NotNull
    private String name;

    @NotNull
    private int quantity;

    private Integer price;

    private String observation;

    @Nullable
    private Integer discount;

    @Nullable
    private DiscountType discountType;

    private List<OrderItemRequest> items;
}
