package net.mymenu.service;

import jakarta.transaction.Transactional;
import net.mymenu.dto.order.OrderItemRequest;
import net.mymenu.enums.DiscountType;
import net.mymenu.enums.order.OrderStatus;
import net.mymenu.exception.NotFoundException;
import net.mymenu.models.Discount;
import net.mymenu.models.Food;
import net.mymenu.models.Order;
import net.mymenu.models.User;
import net.mymenu.models.food_item.FoodItem;
import net.mymenu.models.order.OrderDiscount;
import net.mymenu.models.order.OrderItem;
import net.mymenu.repository.FoodRepository;
import net.mymenu.repository.OrderRepository;
import net.mymenu.repository.food_item.FoodItemRepository;
import net.mymenu.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public OrderItem createOrderItem(OrderItemRequest orderItemRequest) {
        Food food = foodRepository.findById(orderItemRequest.getItemId())
                .orElseThrow(() -> new NotFoundException("Food not found"));

        OrderDiscount orderDiscount = null;

        if (orderItemRequest.getDiscountId() != null) {
            Discount discount = food.getActiveDiscount();

            if (!discount.getId().equals(orderItemRequest.getDiscountId())) {
                throw new NotFoundException("Discount not found");
            }

            orderDiscount = createOrderDiscount(discount);
        }

        OrderItem orderItemFood = OrderItem
                .builder()
                .title(food.getName())
                .observation(orderItemRequest.getObservation())
                .description(food.getDescription())
                .unitPrice(food.getPrice())
                .quantity(orderItemRequest.getQuantity())
                .image(food.getImage())
                .category(food.getCategory().getName())
                .build();

        List<OrderItem> orderItemList = new ArrayList<>(List.of());

        Optional.ofNullable(orderItemRequest.getItems())
                .orElse(List.of())
                .forEach(item -> {
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

                    orderItemList.add(orderItemExtraFood);
                });

        orderItemFood.setOrderItems(orderItemList);
        orderItemFood.setDiscount(orderDiscount);

        return orderItemFood;
    }

    private OrderDiscount createOrderDiscount(Discount discount) {
        return OrderDiscount.builder()
                .discount(discount.getDiscount())
                .type(discount.getType())
                .startAt(discount.getStartAt())
                .endAt(discount.getEndAt())
                .build();
    }

    public Order createOrder(List<OrderItem> orderItems) {
        User user = jwtHelper.extractUser();
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

                    return price;
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
}
