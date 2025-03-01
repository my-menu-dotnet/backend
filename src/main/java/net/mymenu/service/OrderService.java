package net.mymenu.service;

import net.mymenu.dto.order.OrderItemRequest;
import net.mymenu.enums.order.OrderStatus;
import net.mymenu.exception.NotFoundException;
import net.mymenu.models.Food;
import net.mymenu.models.Order;
import net.mymenu.models.User;
import net.mymenu.models.food_item.FoodItem;
import net.mymenu.models.order.OrderItem;
import net.mymenu.repository.FoodRepository;
import net.mymenu.repository.food_item.FoodItemRepository;
import net.mymenu.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private JwtHelper jwtHelper;

    public OrderItem createOrderItem(OrderItemRequest orderItemRequest) {
        Food food = foodRepository.findById(orderItemRequest.getItemId())
                .orElseThrow(() -> new NotFoundException("Food not found"));

        OrderItem orderItemFood = OrderItem
                .builder()
                .title(food.getName())
                .description(food.getDescription())
                .unitPrice(food.getPrice())
                .quantity(orderItemRequest.getQuantity())
                .image(food.getImage())
                .category(food.getCategory().getName())
                .build();

        orderItemRequest.getItems().forEach(item -> {
            FoodItem foodItem = foodItemRepository.findById(item.getItemId())
                    .orElseThrow(() -> new NotFoundException("Food not found"));

            OrderItem orderItemExtraFood = OrderItem
                    .builder()
                    .title(foodItem.getTitle())
                    .description(foodItem.getDescription())
                    .unitPrice(foodItem.getPriceIncrease())
                    .quantity(item.getQuantity())
                    .image(foodItem.getImage())
                    .category(foodItem.getCategory().getTitle())
                    .build();

            orderItemFood.getOrderItems().add(orderItemExtraFood);
        });


        return orderItemFood;
    }

    public Order createOrder(List<OrderItem> orderItems) {
        User user = jwtHelper.extractUser();

        double totalPrice = orderItems.stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();

        return Order.builder()
                .orderItems(orderItems)
                .totalPrice(totalPrice)
                .user(user)
                .status(OrderStatus.PENDING)
                .build();
    }
}
