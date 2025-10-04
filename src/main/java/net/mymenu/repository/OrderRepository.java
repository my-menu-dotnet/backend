package net.mymenu.repository;

import net.mymenu.dto.analytics.DailyOrderStatsResponse;
import net.mymenu.dto.analytics.ItemStatsResponse;
import net.mymenu.dto.analytics.MonthlyAverageTicketResponse;
import net.mymenu.enums.order.OrderStatus;
import net.mymenu.models.Order;
import net.mymenu.models.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("SELECT o FROM Order o ORDER BY o.orderNumber DESC LIMIT 1")
    Optional<Order> findLastOrder();

    @Query("SELECT o FROM Order o WHERE o.status = :status AND FUNCTION('DATE', o.createdAt) = FUNCTION('CURRENT_DATE')")
    List<Order> findAllByStatusToday(@Param("status") OrderStatus status);

    @Query("SELECT o FROM Order o WHERE o.status NOT IN ('DELIVERED') OR " +
            "(o.status IN ('DELIVERED') AND FUNCTION('DATE', o.createdAt) = FUNCTION('CURRENT_DATE'))")
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

    @Query("SELECT new net.mymenu.dto.analytics.ItemStatsResponse(oi.title, SUM(oi.quantity)) " +
           "FROM Order o INNER JOIN o.orderItems oi " +
           "WHERE o.createdAt >= :startDate AND o.createdAt <= :endDate " +
           "AND oi.title IS NOT NULL " +
           "GROUP BY oi.title " +
           "ORDER BY SUM(oi.quantity) DESC")
    List<ItemStatsResponse> getItemStatsByDateRange(@Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate);

    @Query("SELECT new net.mymenu.dto.analytics.DailyOrderStatsResponse(" +
           "DATE(o.createdAt), COUNT(o)) " +
           "FROM Order o " +
           "WHERE o.createdAt >= :startDate AND o.createdAt <= :endDate " +
           "GROUP BY DATE(o.createdAt) " +
           "ORDER BY DATE(o.createdAt)")
    List<DailyOrderStatsResponse> getDailyOrderStatsByDateRange(@Param("startDate") LocalDateTime startDate,
                                                               @Param("endDate") LocalDateTime endDate);

    @Query("SELECT new net.mymenu.dto.analytics.MonthlyAverageTicketResponse(" +
           "YEAR(o.createdAt), MONTH(o.createdAt), AVG(o.totalPrice)) " +
           "FROM Order o " +
           "WHERE o.createdAt >= :startDate AND o.createdAt <= :endDate " +
           "AND o.totalPrice IS NOT NULL " +
           "GROUP BY YEAR(o.createdAt), MONTH(o.createdAt) " +
           "ORDER BY YEAR(o.createdAt) DESC, MONTH(o.createdAt) DESC")
    List<MonthlyAverageTicketResponse> getMonthlyAverageTicketLast12Months(@Param("startDate") LocalDateTime startDate,
                                                                          @Param("endDate") LocalDateTime endDate);
}
