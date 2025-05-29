package net.mymenu.food.food_item;

import net.mymenu.exception.NotFoundException;
import net.mymenu.file_storage.FileStorage;
import net.mymenu.file_storage.FileStorageRepository;
import net.mymenu.food.food_item.dto.FoodItemRequest;
import net.mymenu.food.food_item_category.FoodItemCategory;
import net.mymenu.food.food_item_category.FoodItemCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Service
public class FoodItemService {

    @Autowired
    FoodItemCategoryRepository foodItemCategoryRepository;
    @Autowired
    FileStorageRepository fileStorageRepository;
    @Autowired
    FoodItemRepository foodItemRepository;

    public Page<FoodItem> findAllByFoodIdPageable(UUID foodId, Pageable pageable) {
        return foodItemRepository.findAllByFoodId(foodId, pageable);
    }

    public FoodItem findById(UUID id) {
        return foodItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Food item not found"));
    }

    public FoodItem create(FoodItemRequest foodItemRequest) {
        FoodItemCategory foodItemCategory = foodItemCategoryRepository.findById(foodItemRequest.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        FoodItem foodItem = FoodItem
                .builder()
                .category(foodItemCategory)
                .name(foodItemRequest.getName())
                .description(foodItemRequest.getDescription())
                .priceIncrease(foodItemRequest.getPriceIncrease())
                .build();

        if (foodItemRequest.getImageId() != null) {
            FileStorage fileStorage = fileStorageRepository.findById(foodItemRequest.getImageId())
                    .orElseThrow(() -> new NotFoundException("Image not found"));
            foodItem.setImage(fileStorage);
        }

        foodItemRepository.saveAndFlush(foodItem);

        return foodItem;
    }
}
