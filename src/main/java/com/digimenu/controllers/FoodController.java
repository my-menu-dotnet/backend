package com.digimenu.controllers;

import com.digimenu.dto.food.FoodCreate;
import com.digimenu.dto.food.FoodResponse;
import com.digimenu.mapper.FoodMapper;
import com.digimenu.models.Food;
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
    private FoodMapper foodMapper;

    @GetMapping
    public ResponseEntity<FoodResponse[]> list() {
        Food[] food = foodService.findAll();
        FoodResponse[] foodResponse = foodMapper.toFoodResponse(food);

        return ResponseEntity.status(HttpStatus.OK).body(foodResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodResponse> find(@PathVariable UUID id) {
        Food food = foodService.findById(id);
        FoodResponse foodResponse = foodMapper.toFoodResponse(food);

        return ResponseEntity.status(HttpStatus.OK).body(foodResponse);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FoodResponse> create(@RequestBody FoodCreate food) {
        Food newFood = foodService.create(food);
        FoodResponse foodResponse = foodMapper.toFoodResponse(newFood);

        return ResponseEntity.status(HttpStatus.CREATED).body(foodResponse);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody FoodCreate food) {
        foodService.update(id, food);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
