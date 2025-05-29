package net.mymenu.food;

import jakarta.validation.Valid;
import net.mymenu.food.dto.FoodActiveRequest;
import net.mymenu.food.dto.FoodRequest;
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
@PreAuthorize("hasRole('ADMIN')")
public class FoodController {

    @Autowired
    private FoodService foodService;

    @GetMapping
    public ResponseEntity<Page<Food>> listAll(Pageable pageable) {
        Page<Food> foods = foodService.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(foods);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Food> find(@PathVariable UUID id) {
        Food food = foodService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(food);
    }

    @PostMapping
    public ResponseEntity<Food> create(@RequestBody @Valid FoodRequest foodRequest) {
        Food newFood = foodService.createFoodByRequest(foodRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newFood);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody FoodRequest foodRequest) {
        foodService.updateFoodById(id, foodRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        foodService.deleteFoodById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/active/{id}")
    public ResponseEntity<?> updateActive(@PathVariable UUID id, @RequestBody FoodActiveRequest foodRequest) {
        foodService.updateActiveFoodById(id, foodRequest.isActive());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
