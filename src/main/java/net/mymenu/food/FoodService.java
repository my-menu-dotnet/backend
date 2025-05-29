package net.mymenu.food;

import net.mymenu.category.Category;
import net.mymenu.exception.NotFoundException;
import net.mymenu.file_storage.FileStorage;
import net.mymenu.food.dto.FoodRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;

    public Page<Food> findAll(Pageable pageable) {
        return foodRepository.findAll(pageable);
    }

    public Food findById(UUID id) {
        return foodRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Food not found"));
    }

    public Food createFoodByRequest(FoodRequest request) {
        Food food = new Food();
        updateFoodFromRequest(food, request);
        return foodRepository.save(food);
    }

    public void updateFoodById(UUID id, FoodRequest request) {
        Food food = findById(id);
        updateFoodFromRequest(food, request);
        foodRepository.save(food);
    }

    public void updateActiveFoodById(UUID id, boolean active) {
        Food food = findById(id);
        food.setActive(active);
        foodRepository.save(food);
    }

    public void deleteFoodById(UUID id) {
        foodRepository.deleteById(id);
    }

    private void updateFoodFromRequest(Food food, FoodRequest request) {
        food.setName(request.getName());
        food.setDescription(request.getDescription());
        food.setPrice(request.getPrice());
        food.setActive(request.isActive());

        if (request.getImageId() != null) {
            food.setImage(FileStorage.builder().id(request.getImageId()).build());
        } else {
            food.setImage(null);
        }

        Category category = new Category();
        category.setId(request.getCategoryId());
        food.setCategory(category);

        food.setLactoseFree(request.isLactoseFree());
        food.setGlutenFree(request.isGlutenFree());
        food.setVegan(request.isVegan());
        food.setVegetarian(request.isVegetarian());
        food.setHalal(request.isHalal());
    }
}
