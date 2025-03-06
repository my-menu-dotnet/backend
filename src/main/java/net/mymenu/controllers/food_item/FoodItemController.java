package net.mymenu.controllers.food_item;

import jakarta.transaction.Transactional;
import jakarta.websocket.server.PathParam;
import net.mymenu.dto.food.food_item.FoodItemRequest;
import net.mymenu.exception.NotFoundException;
import net.mymenu.models.FileStorage;
import net.mymenu.models.food_item.FoodItem;
import net.mymenu.models.food_item.FoodItemCategory;
import net.mymenu.repository.FileStorageRepository;
import net.mymenu.repository.food_item.FoodItemCategoryRepository;
import net.mymenu.repository.food_item.FoodItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/food/category/{categoryId}/item")
public class FoodItemController {

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private FoodItemCategoryRepository foodItemCategoryRepository;

    @Autowired
    private FileStorageRepository fileStorageRepository;

    @PostMapping
    public ResponseEntity<FoodItem> create(@PathVariable UUID categoryId, @RequestBody FoodItemRequest foodItemRequest) {
        FoodItemCategory foodItemCategory = foodItemCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        FileStorage fileStorage = null;
        if (foodItemRequest.getImageId() != null) {
            fileStorage = fileStorageRepository.findById(foodItemRequest.getImageId())
                    .orElseThrow(() -> new NotFoundException("Image not found"));
        }

        FoodItem foodItem = FoodItem
                .builder()
                .category(foodItemCategory)
                .title(foodItemRequest.getTitle())
                .description(foodItemRequest.getDescription())
                .priceIncrease(foodItemRequest.getPriceIncrease())
                .image(fileStorage)
                .build();

        foodItemRepository.saveAndFlush(foodItem);

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
        foodItem.setTitle(foodItemRequest.getTitle());
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
