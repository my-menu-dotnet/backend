package net.mymenu.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class OrderItemRequest {

    @JsonProperty("item_id")
    private UUID itemId;

    private int quantity;

    private String observation;

    @JsonProperty("discount_id")
    private UUID discountId;

    private List<OrderItemRequest> items;
}
