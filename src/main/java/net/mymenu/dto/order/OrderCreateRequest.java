package net.mymenu.dto.order;

import lombok.*;
import net.mymenu.dto.AddressRequest;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderCreateRequest {

    private String userName;

    private String companyObservation;

    private List<OrderItemRequest> orderItems;

    private AddressRequest address;
}
