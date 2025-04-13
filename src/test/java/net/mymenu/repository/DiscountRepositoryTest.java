package net.mymenu.repository;

import net.mymenu.config.TenantTest;
import net.mymenu.discount.DiscountRepository;
import net.mymenu.discount.enums.DiscountType;
import net.mymenu.discount.Discount;
import net.mymenu.food.Food;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DiscountRepositoryTest extends TenantTest<Discount> {

    @Autowired
    private DiscountRepository discountRepository;

    private Food food;

    @BeforeEach
    public void createFood() {
        if (this.food != null) {
            return;
        }

        Food food = getFood();
        Discount discount = Discount.builder()
                .food(food)
                .discount(10.0)
                .type(DiscountType.PERCENTAGE)
                .startAt(LocalDate.now())
                .endAt(LocalDate.now().plusDays(7))
                .active(true)
                .build();

        discountRepository.saveAndFlush(discount);
        this.food = food;
    }

    @Test
    void hasOverlappingActiveDiscount() {
        boolean hasOverlappingActiveDiscount = discountRepository.hasOverlappingActiveDiscount(
                food.getId(),
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2),
                UUID.randomUUID()
        );
        assertTrue(hasOverlappingActiveDiscount);
    }

    @Test
    void hasOverlappingActiveDiscountFalse() {
        boolean hasOverlappingActiveDiscount = discountRepository.hasOverlappingActiveDiscount(
                food.getId(),
                LocalDate.now().plusDays(8),
                LocalDate.now().plusDays(9),
                UUID.randomUUID()
        );
        assertFalse(hasOverlappingActiveDiscount);
    }

    @Test
    void hasOverlappingActiveDiscountFalseWhenDiscountIsInactive() {
        Discount discount = discountRepository.findAll().getFirst();
        discount.setActive(false);

        discountRepository.saveAndFlush(discount);

        boolean hasOverlappingActiveDiscount = discountRepository.hasOverlappingActiveDiscount(
                discount.getFood().getId(),
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2),
                discount.getId()
        );
        assertFalse(hasOverlappingActiveDiscount);
    }

    @Test
    void hasOverlappingActiveDiscountFalseWhenNoOverlapping() {
        boolean hasOverlappingActiveDiscount = discountRepository.hasOverlappingActiveDiscount(
                food.getId(),
                LocalDate.now().plusDays(8),
                LocalDate.now().plusDays(9),
                UUID.randomUUID()
        );
        assertFalse(hasOverlappingActiveDiscount);
    }

    @Override
    protected Discount getEntity() {
        Food food = getFood();
        return Discount.builder()
                .food(food)
                .discount(10.0)
                .type(DiscountType.PERCENTAGE)
                .startAt(LocalDate.now())
                .endAt(LocalDate.now().plusDays(7))
                .active(true)
                .build();
    }

    @Override
    protected JpaRepository<Discount, UUID> getRepository() {
        return discountRepository;
    }

    protected Food getFood() {
        return Food.builder()
                .name("Test Food")
                .description("Test Description")
                .price(10.0)
                .build();
    }
}