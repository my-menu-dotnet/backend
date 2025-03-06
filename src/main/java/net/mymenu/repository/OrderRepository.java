package net.mymenu.repository;

import net.mymenu.enums.order.OrderStatus;
import net.mymenu.models.Order;
import net.mymenu.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("SELECT o FROM Order o WHERE o.user = :user " +
            "ORDER BY CASE o.status " +
            "WHEN 'CREATED' THEN 1 " +
            "WHEN 'ACCEPTED' THEN 2 " +
            "WHEN 'PRODUCING' THEN 3 " +
            "WHEN 'READY' THEN 4 " +
            "WHEN 'DELIVERED' THEN 5 " +
            "WHEN 'CANCELLED' THEN 6 " +
            "ELSE 7 END, o.createdAt DESC")
    Page<Order> findAllByUserOrderByStatus(Pageable pageable, @Param("user") User user);

    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.user = :user")
    Double findTotalSumByUser(@Param("user") User user);
}
