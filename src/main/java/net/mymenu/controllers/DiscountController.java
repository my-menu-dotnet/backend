package net.mymenu.controllers;

import jakarta.validation.Valid;
import net.mymenu.dto.discount.DiscountRequest;
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
    public ResponseEntity<Discount> createSale(@RequestBody @Valid DiscountRequest discountRequest) {
        User user = jwtHelper.extractUser();

        List<Food> userFoods = user.getCompanies().getFirst().getCategories().stream()
                .flatMap(category -> category.getFoods().stream())
                .toList();

        if (userFoods.stream().noneMatch(food -> food.getId().equals(discountRequest.getFoodId()))) {
            throw new NotFoundException("Food not found");
        }

        Food food = foodRepository.findById(discountRequest.getFoodId())
                .orElseThrow(() -> new NotFoundException("Food not found"));

        Discount discount = Discount
                .builder()
                .company(user.getCompanies().getFirst())
                .type(discountRequest.getType())
                .status(discountRequest.getStatus())
                .startAt(discountRequest.getStartAt())
                .endAt(discountRequest.getEndAt())
                .discount(discountRequest.getDiscount())
                .build();

        List<Discount> foodDiscounts = food.getDiscounts();
        foodDiscounts.add(discount);

        food.setDiscounts(foodDiscounts);

        discountRepository.saveAndFlush(discount);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(discount);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Discount> updateSale(DiscountRequest discountRequest, @PathVariable UUID id) {
        Discount sale = discountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Discount not found"));

        sale.setStartAt(discountRequest.getStartAt());
        sale.setEndAt(discountRequest.getEndAt());
        sale.setDiscount(discountRequest.getDiscount());

        discountRepository.saveAndFlush(sale);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(sale);
    }

}
