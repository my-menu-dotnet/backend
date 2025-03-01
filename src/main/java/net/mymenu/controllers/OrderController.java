package net.mymenu.controllers;

import net.mymenu.dto.order.OrderItemRequest;
import net.mymenu.models.Order;
import net.mymenu.models.order.OrderItem;
import net.mymenu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody List<OrderItemRequest> orderItemRequests) {
        List<OrderItem> orderItems = orderItemRequests.stream()
                .map(orderService::createOrderItem)
                .toList();

        Order order = orderService.createOrder(orderItems);

        return ResponseEntity.ok(order);
    }
}
