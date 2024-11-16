package com.digimenu.service;

import com.digimenu.dto.food.FoodCreate;
import com.digimenu.exception.NotFoundException;
import com.digimenu.models.Category;
import com.digimenu.models.FileStorage;
import com.digimenu.models.Food;
import com.digimenu.repository.CategoryRepository;
import com.digimenu.repository.FileStorageRepository;
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
    private FileStorageRepository fileStorageRepository;

    @Autowired
    private JwtHelper jwtHelper;

    @Deprecated
    public Food[] findAll() {
        return foodRepository.findAll()
                .toArray(new Food[0]);
    }

    public Food findById(UUID id) {
        return foodRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Food not found"));
    }

    public Food create(FoodCreate food) {
        Category category = categoryRepository.findByIdAndCompanyId(food.getCategoryId(), food.getCompanyId())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        Food newFood = new Food(
                food.getName(),
                food.getDescription(),
                food.getPrice(),
                food.getStatus(),
                category
        );

        return foodRepository.save(newFood);
    }

    public void update(UUID id, FoodCreate food) {
        UUID companyId = food.getCompanyId();

        Category category = categoryRepository.findByIdAndCompanyId(food.getCategoryId(), companyId)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        Food existingFood = foodRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new NotFoundException("Food not found"));
        FileStorage image = fileStorageRepository.findById(food.getImageId())
                .orElse(null);

        existingFood.setName(food.getName());
        existingFood.setDescription(food.getDescription());
        existingFood.setPrice(food.getPrice());
        existingFood.setStatus(food.getStatus());
        existingFood.setImage(image);
        existingFood.setCategory(category);
    }
}
