package net.mymenu.order;

import jakarta.transaction.Transactional;
import net.mymenu.address.Address;
import net.mymenu.address.AddressService;
import net.mymenu.order.dto.OrderCreateRequest;
import net.mymenu.order.dto.OrderFilter;
import net.mymenu.order.dto.OrderItemRequest;
import net.mymenu.discount.enums.DiscountType;
import net.mymenu.order.dto.OrderRequest;
import net.mymenu.order.enums.OrderStatus;
import net.mymenu.exception.NotFoundException;
import net.mymenu.discount.Discount;
import net.mymenu.food.Food;
import net.mymenu.order.order_item.OrderItemRepository;
import net.mymenu.user.User;
import net.mymenu.food.food_item.FoodItem;
import net.mymenu.order.order_discount.OrderDiscount;
import net.mymenu.order.order_item.OrderItem;
import net.mymenu.food.FoodRepository;
import net.mymenu.food.food_item.FoodItemRepository;
import net.mymenu.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private FoodItemRepository foodItemRepository;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AddressService addressService;
    @Autowired
    private OrderItemRepository orderItemRepository;

    public Page<Order> findAll(Pageable pageable, OrderFilter orderFilter) {
        if (Optional.ofNullable(orderFilter).isEmpty()) {
            return orderRepository.findAll(pageable);
        }

        if (Optional.ofNullable(orderFilter.getDate()).isPresent()) {
            LocalDateTime startDate = orderFilter.getDate().atStartOfDay();
            LocalDateTime endDate = orderFilter.getDate().plusDays(1).atStartOfDay().minusSeconds(1);

            List<Order> orders = orderRepository.findAllByCreatedAtBetween(startDate, endDate);

            return new PageImpl<>(orders, pageable, orders.size());
        }

        return orderRepository.findAll(pageable);
    }

    @Transactional
    public Order createOrderByCompanyPanel(OrderCreateRequest orderRequest) {
        Address address = addressService.createAddressFromAddressRequest(orderRequest.getAddress());
        List<OrderItem> orderItems = orderRequest.getOrderItems().stream()
                .map(this::createOrderItem)
                .toList();
        Order order = createOrder(orderItems, null);

        order.setAddress(address);
        order.setUserName(orderRequest.getUserName());

        orderItemRepository.saveAllAndFlush(orderItems);
        orderRepository.save(order);

        return order;
    }

    public OrderItem createOrderItem(OrderItemRequest orderItemRequest) {
        OrderItem orderItemFood = OrderItem
                .builder()
                .name(orderItemRequest.getName())
                .quantity(orderItemRequest.getQuantity())
                .observation(orderItemRequest.getObservation())
                .build();

        if (Optional.ofNullable(orderItemRequest.getDiscount()).isPresent()) {
            OrderDiscount discount = OrderDiscount
                    .builder()
                    .discount(orderItemRequest.getDiscount())
                    .type(orderItemRequest.getDiscountType())
                    .build();
            orderItemFood.setDiscount(discount);
        }

        if (Optional.ofNullable(orderItemRequest.getFoodId()).isPresent()) {
            Food food = foodRepository.findById(orderItemRequest.getFoodId())
                    .orElseThrow(() -> new NotFoundException("Food not found"));
            orderItemFood.setFood(food);
        }

        if (orderItemRequest.getItems() != null && !orderItemRequest.getItems().isEmpty()) {
            List<OrderItem> childItems = orderItemRequest.getItems().stream()
                    .map(this::createOrderItem)
                    .toList();
            orderItemFood.setOrderItems(childItems);
        }

        return orderItemFood;
    }

    public Order createOrder(List<OrderItem> orderItems) {
        User user = jwtHelper.extractUser();
        return createOrder(orderItems, user);
    }

    public Order createOrder(List<OrderItem> orderItems, User user) {
        Order lastOrder = orderRepository.findLastOrder()
                .orElse(null);

        double totalPrice = orderItems.stream()
                .mapToDouble((orderItem) -> {
                    double price = orderItem.getUnitPrice() * orderItem.getQuantity();

                    OrderDiscount discount = orderItem.getDiscount();

                    if (discount != null && discount.getType() == DiscountType.PERCENTAGE) {
                        price = price - (price * discount.getDiscount() / 100);
                    }
                    if (discount != null && discount.getType() == DiscountType.AMOUNT) {
                        price = price - discount.getDiscount();
                    }

                    double itemsPrice = orderItem.getOrderItems().stream()
                            .mapToDouble((item) -> item.getUnitPrice() * item.getQuantity())
                            .sum();

                    return price + itemsPrice;
                })
                .sum();

        int orderNumber = 1;
        if (lastOrder != null) {
            orderNumber = lastOrder.getOrderNumber() + 1;
        }

        return Order.builder()
                .orderItems(orderItems)
                .totalPrice(totalPrice)
                .user(user)
                .status(OrderStatus.CREATED)
                .orderNumber(orderNumber)
                .build();
    }

    public void adjustPositionsForInsert(List<Order> orders, int insertPosition) {
        int currentPosition = 1;

        for (Order o : orders) {
            if (currentPosition == insertPosition) {
                currentPosition++;
            }
            o.setOrder(currentPosition++);
        }

        orderRepository.saveAll(orders);
    }

    public void reorderList(List<Order> orders) {
        for (int i = 0; i < orders.size(); i++) {
            orders.get(i).setOrder(i + 1);
        }
        orderRepository.saveAll(orders);
    }
}
