package net.mymenu.repository;

import net.mymenu.models.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.UUID;

public interface DiscountRepository extends JpaRepository<Discount, UUID> {

    @Query("SELECT COUNT(d) > 0 FROM Discount d " +
            "WHERE d.food.id = :foodId " +
            "AND d.active = true " +
            "AND d.id != :discountId " +
            "AND ((d.startAt BETWEEN :startAt AND :endAt) " +
            "OR (d.endAt BETWEEN :startAt AND :endAt) " +
            "OR (:startAt BETWEEN d.startAt AND d.endAt))")
    boolean hasOverlappingActiveDiscount(
            @Param("foodId") UUID foodId,
            @Param("startAt") LocalDate startAt,
            @Param("endAt") LocalDate endAt,
            @Param("discountId") UUID discountId
    );
}
