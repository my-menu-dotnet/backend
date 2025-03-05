package net.mymenu.repository;

import net.mymenu.enums.order.OrderStatus;
import net.mymenu.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("SELECT o FROM Order o ORDER BY o.orderNumber DESC LIMIT 1")
    Optional<Order> findLastOrder();

    List<Order> findAllByStatus(OrderStatus status);


    @Query("SELECT o FROM Order o WHERE o.status NOT IN ('READY', 'DELIVERED') OR " +
            "(o.status IN ('READY', 'DELIVERED') AND FUNCTION('DATE', o.updatedAt) = FUNCTION('CURRENT_DATE'))")
    List<Order> findAllExcludeOldOrders();
}
