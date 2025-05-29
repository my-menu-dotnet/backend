package net.mymenu.food.food_item;

import jakarta.transaction.Transactional;
import net.mymenu.food.food_item.dto.FoodItemRequest;
import net.mymenu.exception.NotFoundException;
import net.mymenu.file_storage.FileStorage;
import net.mymenu.food.food_item_category.FoodItemCategory;
import net.mymenu.file_storage.FileStorageRepository;
import net.mymenu.food.food_item_category.FoodItemCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/food-item")
public class FoodItemController {

    @Autowired
    private FoodItemRepository foodItemRepository;
    @Autowired
    private FoodItemCategoryRepository foodItemCategoryRepository;
    @Autowired
    private FileStorageRepository fileStorageRepository;
    @Autowired
    private FoodItemService foodItemService;

    @GetMapping("/food/{foodId}")
    public ResponseEntity<Page<FoodItem>> findAll(@PathVariable UUID foodId, Pageable pageable) {
        Page<FoodItem> foodItems = foodItemService.findAllByFoodIdPageable(foodId, pageable);
        return ResponseEntity.ok(foodItems);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodItem> findById(@PathVariable UUID id) {
        FoodItem foodItem = foodItemService.findById(id);
        return ResponseEntity.ok(foodItem);
    }

    @PostMapping
    public ResponseEntity<FoodItem> create(@RequestBody FoodItemRequest foodItemRequest) {
        FoodItem foodItem = foodItemService.create(foodItemRequest);
        return ResponseEntity.ok(foodItem);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> update(@PathVariable UUID categoryId, @PathVariable UUID id, @RequestBody FoodItemRequest foodItemRequest) {
        FoodItem foodItem = foodItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item not found"));

        FoodItemCategory foodItemCategory = foodItemCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        FileStorage fileStorage = foodItem.getImage();
        if (foodItemRequest.getImageId() != null) {
            fileStorage = fileStorageRepository.findById(foodItemRequest.getImageId())
                    .orElseThrow(() -> new NotFoundException("Image not found"));
        }

        foodItem.setDescription(foodItemRequest.getDescription());
        foodItem.setName(foodItemRequest.getName());
        foodItem.setCategory(foodItemCategory);
        foodItem.setPriceIncrease(foodItemRequest.getPriceIncrease());
        foodItem.setImage(fileStorage);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/order")
    @Transactional
    public ResponseEntity<Void> updateOrder(@PathVariable UUID categoryId, @RequestBody List<UUID> foodItemIds) {
        FoodItemCategory foodItemCategory = foodItemCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Items not found"));

        List<FoodItem> foodItems = foodItemCategory.getFoodItems();

        foodItems.forEach(foodItem -> {
            foodItem.setOrder(foodItemIds.indexOf(foodItem.getId()));
        });

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> remove(@PathVariable UUID id) {
        foodItemRepository.removeById(id);
        return ResponseEntity.noContent().build();
    }
}
