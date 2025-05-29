package net.mymenu.food.food_item_category;

import net.mymenu.exception.NotFoundException;
import net.mymenu.food.Food;
import net.mymenu.food.FoodRepository;
import net.mymenu.food.food_item_category.dto.FoodItemCategoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FoodItemCategoryService {

    @Autowired
    FoodItemCategoryRepository foodItemCategoryRepository;
    @Autowired
    private FoodRepository foodRepository;

    public Map<UUID, String> selectByFoodId(UUID foodId) {
        List<FoodItemCategory> foodItemCategoryList = foodItemCategoryRepository.findAllByFoodId(foodId);
        return foodItemCategoryList.stream()
                .collect(Collectors.toMap(FoodItemCategory::getId, FoodItemCategory::getName));
    }

    public Page<FoodItemCategory> findAllByFoodId(UUID foodId, Pageable pageable) {
        return foodItemCategoryRepository.findAllByFoodId(foodId, pageable);
    }

    private void fillCategoryFromRequest(FoodItemCategory category, Food food, FoodItemCategoryRequest request) {
        category.setFood(food);
        category.setMaxItems(request.getMaxItems());
        category.setMinItems(request.getMinItems());
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setRequired(request.getRequired());
    }

    public FoodItemCategory createByRequest(FoodItemCategoryRequest request) {
        Food food = foodRepository.findById(request.getFoodId())
                .orElseThrow(() -> new NotFoundException("Food not found"));

        FoodItemCategory lastFoodItemCategory = food.getItemCategories()
                .stream()
                .max(Comparator.comparingInt(FoodItemCategory::getOrder))
                .orElse(null);

        FoodItemCategory foodItemCategory = new FoodItemCategory();
        fillCategoryFromRequest(foodItemCategory, food, request);
        foodItemCategory.setOrder(lastFoodItemCategory != null ? lastFoodItemCategory.getOrder() + 1 : 0);

        foodItemCategoryRepository.saveAndFlush(foodItemCategory);

        return foodItemCategory;
    }

    public void updateById(UUID id, FoodItemCategoryRequest request) {
        FoodItemCategory foodItemCategory = foodItemCategoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        Food food = foodRepository.findById(request.getFoodId())
                .orElseThrow(() -> new NotFoundException("Food not found"));

        fillCategoryFromRequest(foodItemCategory, food, request);
    }

    public void deleteById(UUID id) {
        foodItemCategoryRepository.deleteById(id);
    }

    public FoodItemCategory findById(UUID id) {
        return foodItemCategoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Food item category not found"));
    }
}
