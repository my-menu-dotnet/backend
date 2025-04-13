package net.mymenu.food.food_item_category;

import jakarta.transaction.Transactional;
import net.mymenu.food.food_item.dto.FoodItemCategoryRequest;
import net.mymenu.exception.NotFoundException;
import net.mymenu.food.Food;
import net.mymenu.food.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/food/{food_id}/category")
public class FoodItemCategoryController {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private FoodItemCategoryRepository foodItemCategoryRepository;

    @PostMapping
    public ResponseEntity<FoodItemCategory> create(
            @PathVariable UUID food_id,
            @RequestBody FoodItemCategoryRequest foodItemCategoryRequest) {
        Food food = foodRepository.findById(food_id)
                .orElseThrow(() -> new NotFoundException("Food not found"));

        FoodItemCategory lastFoodItemCategory = food.getItemCategories()
                .stream()
                .max(Comparator.comparingInt(FoodItemCategory::getOrder))
                .orElse(null);

        FoodItemCategory foodItemCategory = FoodItemCategory
                .builder()
                .food(food)
                .minItems(foodItemCategoryRequest.getMinItems())
                .maxItems(foodItemCategoryRequest.getMinItems())
                .title(foodItemCategoryRequest.getTitle())
                .description(foodItemCategoryRequest.getDescription())
                .required(foodItemCategoryRequest.getRequired())
                .order(lastFoodItemCategory != null ? lastFoodItemCategory.getOrder() + 1 : 0)
                .build();

        foodItemCategoryRepository.saveAndFlush(foodItemCategory);

        return ResponseEntity.ok(foodItemCategory);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> update(
            @PathVariable UUID food_id,
            @PathVariable UUID id,
            @RequestBody FoodItemCategoryRequest foodItemCategoryRequest
    ) {
        FoodItemCategory foodItemCategory = foodItemCategoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        Food food = foodRepository.findById(food_id)
                .orElseThrow(() -> new NotFoundException("Food not found"));

        foodItemCategory.setFood(food);
        foodItemCategory.setMaxItems(foodItemCategoryRequest.getMaxItems());
        foodItemCategory.setMinItems(foodItemCategoryRequest.getMinItems());
        foodItemCategory.setTitle(foodItemCategoryRequest.getTitle());
        foodItemCategory.setDescription(foodItemCategoryRequest.getDescription());
        foodItemCategory.setRequired(foodItemCategoryRequest.getRequired());

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/order")
    @Transactional
    public ResponseEntity<Void> updateOrder(
            @PathVariable UUID food_id,
            @RequestBody List<UUID> categoryIds
    ) {
        Food food = foodRepository.findById(food_id)
                .orElseThrow(() -> new NotFoundException("Food not found"));

        List<FoodItemCategory> foodItemCategories = food.getItemCategories();

        foodItemCategories.forEach(foodItemCategory -> {
            foodItemCategory.setOrder(categoryIds.indexOf(foodItemCategory.getId()));
        });

        return ResponseEntity.noContent().build();
    }
}
