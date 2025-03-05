package net.mymenu.controllers;

import jakarta.transaction.Transactional;
import net.mymenu.dto.order.OrderItemRequest;
import net.mymenu.dto.order.OrderRequest;
import net.mymenu.enums.order.OrderStatus;
import net.mymenu.exception.DifferentTotalsOrder;
import net.mymenu.models.Order;
import net.mymenu.models.order.OrderItem;
import net.mymenu.repository.OrderRepository;
import net.mymenu.service.OrderService;
import net.mymenu.service.OrderWebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderWebSocketService orderWebSocketService;

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody List<OrderItemRequest> orderItemRequests, @RequestParam Double total) {
        List<OrderItem> orderItems = orderItemRequests.stream()
                .map(orderService::createOrderItem)
                .toList();

        Order order = orderService.createOrder(orderItems);

        if (total != order.getTotalPrice()) {
            throw new DifferentTotalsOrder();
        }

        orderRepository.save(order);

        UUID tenantId = order.getUser().getCompany().getId();
        orderWebSocketService.sendNotificationToTenant(tenantId, order);

        return ResponseEntity.ok(order);
    }

    @GetMapping("/kanban")
    public ResponseEntity<List<Order>> findAll() {
        return ResponseEntity.ok(orderRepository.findAllExcludeOldOrders());
    }

    @GetMapping
    public ResponseEntity<Page<Order>> findAllTable(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        return ResponseEntity.ok(orders);
    }

    @PatchMapping("/{orderId}")
    @Transactional
    public ResponseEntity<Order> updateStatus(@PathVariable UUID orderId, @RequestBody OrderRequest orderRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderStatus oldStatus = order.getStatus();
        OrderStatus newStatus = orderRequest.getStatus();
        int newPosition = orderRequest.getNewOrder();

        order.setStatus(newStatus);
        order.setOrder(newPosition);

        if (oldStatus != newStatus) {
            List<Order> oldStatusOrders = orderRepository.findAllByStatus(oldStatus).stream()
                    .filter(o -> !o.getId().equals(orderId))
                    .toList();
            reorderList(oldStatusOrders);

            List<Order> newStatusOrders = orderRepository.findAllByStatus(newStatus).stream()
                    .filter(o -> !o.getId().equals(orderId))
                    .sorted(Comparator.comparingInt(Order::getOrder))
                    .toList();

            adjustPositionsForInsert(newStatusOrders, newPosition);
        } else {
            List<Order> sameStatusOrders = orderRepository.findAllByStatus(newStatus).stream()
                    .filter(o -> !o.getId().equals(orderId))
                    .sorted(Comparator.comparingInt(Order::getOrder))
                    .toList();

            adjustPositionsForInsert(sameStatusOrders, newPosition);
        }

        orderRepository.save(order);
        return ResponseEntity.ok(order);
    }

    private void adjustPositionsForInsert(List<Order> orders, int insertPosition) {
        int currentPosition = 1;

        for (Order o : orders) {
            if (currentPosition == insertPosition) {
                currentPosition++;
            }
            o.setOrder(currentPosition++);
        }

        orderRepository.saveAll(orders);
    }

    private void reorderList(List<Order> orders) {
        for (int i = 0; i < orders.size(); i++) {
            orders.get(i).setOrder(i + 1);
        }
        orderRepository.saveAll(orders);
    }
}