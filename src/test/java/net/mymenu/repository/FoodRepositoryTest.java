package net.mymenu.repository;

import net.mymenu.config.TenantTest;
import net.mymenu.food.Food;
import net.mymenu.food.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public class FoodRepositoryTest extends TenantTest<Food> {

    @Autowired
    private FoodRepository foodRepository;

    @Override
    protected Food getEntity() {
        return Food.builder()
                .name("Food to Delete")
                .price(10.0)
                .build();
    }

    @Override
    protected JpaRepository<Food, UUID> getRepository() {
        return foodRepository;
    }
}