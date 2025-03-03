package net.mymenu.controllers;

import com.mercadopago.resources.preference.Preference;
import net.mymenu.dto.order.OrderItemRequest;
import net.mymenu.dto.order.OrderResponse;
import net.mymenu.models.Order;
import net.mymenu.models.order.OrderItem;
import net.mymenu.service.MercadoPagoService;
import net.mymenu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MercadoPagoService mercadoPagoService;

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody List<OrderItemRequest> orderItemRequests, @RequestParam Double total) {
        List<OrderItem> orderItems = orderItemRequests.stream()
                .map(orderService::createOrderItem)
                .toList();

        Order order = orderService.createOrder(orderItems);

        Preference preference = mercadoPagoService.createPreference(order);

        return ResponseEntity.ok(OrderResponse
                .builder()
                .preferenceId(preference.getId())
                .build());
    }
}
