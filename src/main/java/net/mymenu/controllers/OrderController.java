package net.mymenu.controllers;

import jakarta.transaction.Transactional;
import net.mymenu.dto.order.*;
import net.mymenu.enums.order.OrderStatus;
import net.mymenu.exception.DifferentTotalsOrder;
import net.mymenu.models.Address;
import net.mymenu.models.Order;
import net.mymenu.models.User;
import net.mymenu.models.order.OrderItem;
import net.mymenu.repository.OrderRepository;
import net.mymenu.repository.order.OrderItemRepository;
import net.mymenu.security.JwtHelper;
import net.mymenu.service.AddressService;
import net.mymenu.service.OrderService;
import net.mymenu.service.OrderWebSocketService;
import net.mymenu.tenant.TenantContext;
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
    private JwtHelper jwtHelper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderWebSocketService orderWebSocketService;

    @Autowired
    private AddressService addressService;

    @GetMapping("/user")
    public ResponseEntity<Page<Order>> findUserOrder(Pageable pageable) {
        User user = jwtHelper.extractUser();
        Page<Order> order = orderRepository.findAllByUserOrderByStatus(pageable, user);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/user/total")
    public ResponseEntity<OrderTotalDTO> findUserOrderTotalSum() {
        User user = jwtHelper.extractUser();
        Double total = orderRepository.findTotalSumByUser(user);
        return ResponseEntity.ok(new OrderTotalDTO(total));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Order> create(@RequestBody List<OrderItemRequest> orderItemRequests, @RequestParam Double total) {
        List<OrderItem> orderItems = orderItemRequests.stream()
                .map(orderService::createOrderItem)
                .toList();

        Order order = orderService.createOrder(orderItems);

        if (total != order.getTotalPrice()) {
            throw new DifferentTotalsOrder();
        }

        orderItemRepository.saveAllAndFlush(orderItems);
        orderRepository.save(order);

        orderWebSocketService.sendNotificationToTenant(TenantContext.getCurrentTenant(), order);

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
            orderService.reorderList(oldStatusOrders);

            List<Order> newStatusOrders = orderRepository.findAllByStatus(newStatus).stream()
                    .filter(o -> !o.getId().equals(orderId))
                    .sorted(Comparator.comparingInt(Order::getOrder))
                    .toList();

            orderService.adjustPositionsForInsert(newStatusOrders, newPosition);
        } else {
            List<Order> sameStatusOrders = orderRepository.findAllByStatus(newStatus).stream()
                    .filter(o -> !o.getId().equals(orderId))
                    .sorted(Comparator.comparingInt(Order::getOrder))
                    .toList();

            orderService.adjustPositionsForInsert(sameStatusOrders, newPosition);
        }

        orderRepository.save(order);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/anonymously")
    @Transactional
    public ResponseEntity<Order> createManual(@RequestBody OrderCreateRequest orderRequest) {
        Address address = addressService.createAddressFromAddressRequest(orderRequest.getAddress());

        List<OrderItem> orderItems = orderRequest.getOrderItems().stream()
                .map(orderService::createOrderItem)
                .toList();

        Order order = orderService.createOrder(orderItems, null);

        order.setAddress(address);
        order.setUserName(orderRequest.getUserName());
        order.setCompanyObservation(orderRequest.getCompanyObservation());

        orderItemRepository.saveAllAndFlush(orderItems);
        orderRepository.save(order);

        orderWebSocketService.sendNotificationToTenant(TenantContext.getCurrentTenant(), order);

        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/{orderId}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderStatus status = order.getStatus();

        if (status != OrderStatus.CREATED && status != OrderStatus.ACCEPTED) {
            throw new RuntimeException("Order cannot be deleted");
        }

        orderRepository.delete(order);

        return ResponseEntity.noContent().build();
    }
}