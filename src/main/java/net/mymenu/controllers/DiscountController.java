package net.mymenu.controllers;

import jakarta.validation.Valid;
import net.mymenu.dto.sale.SaleRequest;
import net.mymenu.exception.NotFoundException;
import net.mymenu.models.Food;
import net.mymenu.models.Discount;
import net.mymenu.models.User;
import net.mymenu.repository.FoodRepository;
import net.mymenu.repository.DiscountRepository;
import net.mymenu.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/discount")
public class DiscountController {

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private JwtHelper jwtHelper;

    @GetMapping
    public ResponseEntity<List<Discount>> findSale() {
        User user = jwtHelper.extractUser();

        List<Discount> sale = discountRepository.findAllByCompany((user.getCompanies().getFirst()))
                .orElseThrow(() -> new NotFoundException("Discount not found"));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(sale);
    }

    @PostMapping
    public ResponseEntity<Discount> createSale(@RequestBody @Valid SaleRequest saleRequest) {
        User user = jwtHelper.extractUser();

        List<Food> userFoods = user.getCompanies().getFirst().getCategories().stream()
                .flatMap(category -> category.getFoods().stream())
                .toList();

        if (userFoods.stream().noneMatch(food -> food.getId().equals(saleRequest.getFoodId()))) {
            throw new NotFoundException("Food not found");
        }

        if (saleRequest.getDiscount() < 0 || saleRequest.getDiscount() > 100) {
            throw new NotFoundException("Discount must be between 0 and 100");
        }

        Food food = foodRepository.findById(saleRequest.getFoodId())
                .orElseThrow(() -> new NotFoundException("Food not found"));

        Discount sale = Discount
                .builder()
                .startAt(saleRequest.getStartAt())
                .endAt(saleRequest.getEndAt())
                .discount(saleRequest.getDiscount())
                .build();

        List<Discount> foodDiscounts = food.getDiscounts();
        foodDiscounts.add(sale);

        food.setDiscounts(foodDiscounts);

        discountRepository.saveAndFlush(sale);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(sale);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Discount> updateSale(SaleRequest saleRequest, @PathVariable UUID id) {
        Discount sale = discountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Discount not found"));

        sale.setStartAt(saleRequest.getStartAt());
        sale.setEndAt(saleRequest.getEndAt());
        sale.setDiscount(saleRequest.getDiscount());

        discountRepository.saveAndFlush(sale);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(sale);
    }

}
