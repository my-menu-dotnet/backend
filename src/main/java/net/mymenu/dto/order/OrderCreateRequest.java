package net.mymenu.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import net.mymenu.dto.AddressRequest;

import java.util.List;
import java.util.UUID;

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

    /**
     * Optional. When set, the manual order is linked to the existing client
     * (the client's name and address are reused). When null, a new Client is
     * created from {@code userName} + {@code address}.
     */
    @JsonProperty("client_id")
    private UUID clientId;
}
