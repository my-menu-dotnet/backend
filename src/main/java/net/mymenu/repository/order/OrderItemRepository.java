package net.mymenu.repository.order;

import net.mymenu.models.Address;
import net.mymenu.models.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
}
