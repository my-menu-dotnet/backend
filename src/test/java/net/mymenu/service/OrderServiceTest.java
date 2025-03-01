package net.mymenu.service;

import net.mymenu.config.TestSecurityConfig;
import net.mymenu.dto.order.OrderItemRequest;
import net.mymenu.models.User;
import net.mymenu.models.order.OrderItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestSecurityConfig.class)
class OrderServiceTest {

//    @Mock
//    private OrderService orderService;
//
//    private List<OrderItemRequest> orderItemRequests = List.of(
//            OrderItemRequest.builder()
//                    .itemId(UUID.randomUUID())
//                    .quantity(1)
//                    .items(List.of(
//                            OrderItemRequest.builder()
//                                    .itemId(UUID.randomUUID())
//                                    .quantity(1)
//                                    .build()
//                    ))
//                    .build()
//    );
//
//    @Test
//    @WithUserDetails("admin@test.com")
//    void createOrderItem() {
//        OrderItemRequest first = orderItemRequests.getFirst();
//
//        OrderItem orderItem = orderService.createOrderItem(first);
//
//        double totalItem = orderItem.getQuantity() * orderItem.getUnitPrice();
//        double totalItems = orderItem.getOrderItems().stream()
//                .mapToDouble(item -> item.getQuantity() * item.getUnitPrice())
//                .sum();
//        double total = totalItem + totalItems;
//
//        assertNotNull(orderItem);
//        assertEquals(first.getQuantity(), orderItem.getQuantity());
//        assertEquals(first.getItems().size(), orderItem.getOrderItems().size());
//        assertEquals(first.getQuantity(), orderItem.getOrderItems().getFirst().getQuantity());
//        assertEquals(total, orderItem.getTotalPrice());
//    }
//
//    @Test
//    void createOrder() {
//    }
}