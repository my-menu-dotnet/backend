package net.mymenu.food.food_item_category;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import net.mymenu.food.food_item.FoodItem;
import net.mymenu.food.food_item.FoodItemService;
import net.mymenu.food.food_item_category.dto.FoodItemCategoryRequest;
import net.mymenu.exception.NotFoundException;
import net.mymenu.food.Food;
import net.mymenu.food.FoodRepository;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/food-category")
public class FoodItemCategoryController {

    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private FoodItemCategoryService foodItemCategoryService;

    @GetMapping("/{id}")
    public ResponseEntity<FoodItemCategory> findById(@PathVariable UUID id) {
        FoodItemCategory category = foodItemCategoryService.findById(id);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/food/{foodId}")
    public ResponseEntity<Page<FoodItemCategory>> findAllByFoodId(@PathVariable UUID foodId, Pageable pageable) {
        Page<FoodItemCategory> foodItemCategories = foodItemCategoryService.findAllByFoodId(foodId, pageable);
        return ResponseEntity.ok(foodItemCategories);
    }

    @PostMapping
    public ResponseEntity<FoodItemCategory> create(@RequestBody @Valid FoodItemCategoryRequest foodItemCategoryRequest) {
        FoodItemCategory foodItemCategory = foodItemCategoryService.createByRequest(foodItemCategoryRequest);
        return ResponseEntity.ok(foodItemCategory);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> update(
            @PathVariable UUID id,
            @RequestBody FoodItemCategoryRequest foodItemCategoryRequest
    ) {
        foodItemCategoryService.updateById(id, foodItemCategoryRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        foodItemCategoryService.deleteById(id);
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

    @GetMapping("/food/{foodId}/select")
    public ResponseEntity<Map<UUID, String>> selectByFoodId(@PathVariable UUID foodId) {
        Map<UUID, String> foodItemCategories = foodItemCategoryService.selectByFoodId(foodId);
        return ResponseEntity.ok(foodItemCategories);
    }

}
