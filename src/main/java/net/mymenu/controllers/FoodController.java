package net.mymenu.controllers;

import net.mymenu.dto.food.FoodRequest;
import net.mymenu.exception.NotFoundException;
import net.mymenu.models.*;
import net.mymenu.repository.CategoryRepository;
import net.mymenu.repository.CompanyRepository;
import net.mymenu.repository.FileStorageRepository;
import net.mymenu.repository.FoodRepository;
import net.mymenu.security.JwtHelper;
import net.mymenu.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private JwtHelper jwtHelper;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Food> find(@PathVariable UUID id) {
        Food food = foodService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(food);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Food> create(@RequestBody FoodRequest food) {
        User user = jwtHelper.extractUser();
        Company company = user.getCompanies().getFirst();

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
                .company(company)
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
        User user = jwtHelper.extractUser();
        Company company = user.getCompanies().getFirst();

        Food existingFood = foodRepository.findByIdAndCompanyId(id, company.getId())
                .orElseThrow(() -> new NotFoundException("Food not found"));

        if (!user.getCompanies().contains(existingFood.getCompany())) {
            throw new SecurityException("You are not allowed to update food for this company");
        }

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
