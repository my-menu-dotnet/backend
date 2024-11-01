package com.digimenu.service;

import com.digimenu.dto.food.FoodCreate;
import com.digimenu.exception.NotFoundException;
import com.digimenu.models.Category;
import com.digimenu.models.Food;
import com.digimenu.repository.CategoryRepository;
import com.digimenu.repository.FoodRepository;
import com.digimenu.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private JwtHelper jwtHelper;

    public Food[] findAll() {
        UUID companyId = jwtHelper.extractCompanyId();
        return foodRepository.findAllByCompanyId(companyId)
                .orElseThrow(() -> new NotFoundException("Food not found"));
    }

    public Food findById(UUID id) {
        UUID companyId = jwtHelper.extractCompanyId();
        return foodRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new NotFoundException("Food not found"));
    }

    public Food create(FoodCreate food) {
        UUID companyId = jwtHelper.extractCompanyId();
        Category category = categoryRepository.findByIdAndCompanyId(food.getCategoryId(), companyId)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        Food newFood = new Food(
                food.getName(),
                food.getDescription(),
                food.getPrice(),
                food.getImage(),
                food.getStatus(),
                category
        );

        return foodRepository.save(newFood);
    }

    public void update(UUID id, FoodCreate food) {
        UUID companyId = jwtHelper.extractCompanyId();
        Category category = categoryRepository.findByIdAndCompanyId(food.getCategoryId(), companyId)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        Food existingFood = foodRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new NotFoundException("Food not found"));

        existingFood.setName(food.getName());
        existingFood.setDescription(food.getDescription());
        existingFood.setPrice(food.getPrice());
        existingFood.setImage(food.getImage());
        existingFood.setStatus(food.getStatus());
        existingFood.setCategory(category);
    }
}
