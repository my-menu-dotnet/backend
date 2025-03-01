package net.mymenu.controllers;

import net.mymenu.dto.food.FoodRequest;
import net.mymenu.exception.NotFoundException;
import net.mymenu.models.*;
import net.mymenu.models.Food;
import net.mymenu.repository.CategoryRepository;
import net.mymenu.repository.FileStorageRepository;
import net.mymenu.repository.FoodRepository;
import net.mymenu.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/food")
public class FoodController {

    @Autowired
    private FoodService foodService;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private FileStorageRepository fileStorageRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<Page<Food>> listAll(Pageable pageable) {
        Page<Food> foods = foodRepository.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(foods);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Food> find(@PathVariable UUID id) {
        Food food = foodService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(food);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Food> create(@RequestBody FoodRequest food) {
        FileStorage image = null;
        if (food.getImageId() != null) {
            image = fileStorageRepository.findById(food.getImageId())
                    .orElseThrow(() -> new NotFoundException("File not found"));
        }
        Category category = categoryRepository.findById(food.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        Food newFood = Food
                .builder()
                .name(food.getName())
                .description(food.getDescription())
                .price(food.getPrice())
                .status(food.getStatus())
                .image(image)
                .category(category)
                .glutenFree(food.isGlutenFree())
                .lactoseFree(food.isLactoseFree())
                .vegan(food.isVegan())
                .vegetarian(food.isVegetarian())
                .halal(food.isHalal())
                .build();

        foodRepository.save(newFood);

        return ResponseEntity.status(HttpStatus.CREATED).body(newFood);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody FoodRequest food) {
        Food existingFood = foodRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Food not found"));

        Category category = categoryRepository.findById(food.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));
        FileStorage image = existingFood.getImage();
        if (food.getImageId() != null) {
            image = fileStorageRepository.findById(food.getImageId())
                    .orElseThrow(() -> new NotFoundException("File not found"));
        }

        existingFood.setName(food.getName());
        existingFood.setDescription(food.getDescription());
        existingFood.setPrice(food.getPrice());
        existingFood.setStatus(food.getStatus());
        existingFood.setImage(image);
        existingFood.setCategory(category);
        existingFood.setLactoseFree(food.isLactoseFree());
        existingFood.setGlutenFree(food.isGlutenFree());
        existingFood.setVegan(food.isVegan());
        existingFood.setVegetarian(food.isVegetarian());
        existingFood.setHalal(food.isHalal());

        foodRepository.saveAndFlush(existingFood);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
