package com.digimenu.controllers;

import com.digimenu.dto.food.FoodRequest;
import com.digimenu.exception.NotFoundException;
import com.digimenu.models.*;
import com.digimenu.repository.CategoryRepository;
import com.digimenu.repository.CompanyRepository;
import com.digimenu.repository.FileStorageRepository;
import com.digimenu.repository.FoodRepository;
import com.digimenu.security.JwtHelper;
import com.digimenu.service.FoodService;
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
    private CompanyRepository companyRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<Food[]> list() {
        Food[] food = foodService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(food);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Food> find(@PathVariable UUID id) {
        Food food = foodService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(food);
    }

    @PostMapping("/{companyId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Food> create(@RequestBody FoodRequest food, @PathVariable UUID companyId) {
        User user = jwtHelper.extractUser();

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Company not found"));

        if (!user.getCompanies().contains(company)) {
            throw new SecurityException("You are not allowed to create food for this company");
        }

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
                .build();

        foodRepository.save(newFood);

        return ResponseEntity.status(HttpStatus.CREATED).body(newFood);
    }

    @PutMapping("/{id}/{companyId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@PathVariable UUID id, @PathVariable UUID companyId, @RequestBody FoodRequest food) {
        User user = jwtHelper.extractUser();

        Food existingFood = foodRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new NotFoundException("Food not found"));

        if (!user.getCompanies().contains(existingFood.getCompany())) {
            throw new SecurityException("You are not allowed to update food for this company");
        }

        Category category = categoryRepository.findById(food.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));
        FileStorage image = fileStorageRepository.findById(food.getImageId())
                .orElseThrow(() -> new NotFoundException("File not found"));

        existingFood.setName(food.getName());
        existingFood.setDescription(food.getDescription());
        existingFood.setPrice(food.getPrice());
        existingFood.setStatus(food.getStatus());
        existingFood.setImage(image);
        existingFood.setCategory(category);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
