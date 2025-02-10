package net.mymenu.controllers;

import jakarta.validation.Valid;
import net.mymenu.dto.discount.DiscountDTO;
import net.mymenu.dto.discount.DiscountRequest;
import net.mymenu.enums.DiscountStatus;
import net.mymenu.exception.DuplicateException;
import net.mymenu.exception.NotFoundException;
import net.mymenu.mapper.DiscountMapper;
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

    @Autowired
    private DiscountMapper discountMapper;

    @GetMapping
    public ResponseEntity<List<DiscountDTO>> findSale() {
        User user = jwtHelper.extractUser();

        List<Discount> discounts = discountRepository.findAllByCompanyOrderByStatus((user.getCompanies().getFirst()))
                .orElseThrow(() -> new NotFoundException("Discount not found"));

        List<DiscountDTO> discountDTOS = discountMapper.toDiscountDTO(discounts);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(discountDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscountDTO> findSaleById(@PathVariable UUID id) {
        User user = jwtHelper.extractUser();

        Discount discount = discountRepository.findByIdAndCompany(id, user.getCompanies().getFirst())
                .orElseThrow(() -> new NotFoundException("Discount not found"));

        DiscountDTO discountDTO = discountMapper.toDiscountDTO(discount);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(discountDTO);
    }

    @PostMapping
    public ResponseEntity<DiscountDTO> createSale(@RequestBody @Valid DiscountRequest discountRequest) {
        User user = jwtHelper.extractUser();

        List<Food> userFoods = user.getCompanies().getFirst().getCategories().stream()
                .flatMap(category -> category.getFoods().stream())
                .toList();

        Food food = userFoods.stream()
                .filter(food1 -> food1.getId().equals(discountRequest.getFoodId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Food not found"));

        if (discountRequest.getStatus() == DiscountStatus.ACTIVE
                && food.getDiscounts().stream().anyMatch(discount -> discount.getStatus().equals(DiscountStatus.ACTIVE))) {
            throw new DuplicateException("Food already has a discount active");
        }

        Discount discount = Discount
                .builder()
                .company(user.getCompanies().getFirst())
                .type(discountRequest.getType())
                .status(discountRequest.getStatus())
                .startAt(discountRequest.getStartAt())
                .endAt(discountRequest.getEndAt())
                .discount(discountRequest.getDiscount())
                .food(food)
                .build();

        discountRepository.saveAndFlush(discount);

        DiscountDTO discountDTO = discountMapper.toDiscountDTO(discount);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(discountDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiscountDTO> updateSale(@RequestBody @Valid DiscountRequest discountRequest, @PathVariable UUID id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Discount not found"));

        Food food = discount.getFood();

        if (food.getId() != discountRequest.getFoodId()) {
            food = foodRepository.findById(discountRequest.getFoodId())
                    .orElseThrow(() -> new NotFoundException("Food not found"));
        }

        if (discountRequest.getStatus() == DiscountStatus.ACTIVE
                && food.getDiscounts().stream().anyMatch(d -> d.getStatus().equals(DiscountStatus.ACTIVE))) {
            throw new NotFoundException("Food already has a discount active");
        }

        discount.setType(discountRequest.getType());
        discount.setStatus(discountRequest.getStatus());
        discount.setStartAt(discountRequest.getStartAt());
        discount.setEndAt(discountRequest.getEndAt());
        discount.setDiscount(discountRequest.getDiscount());
        discount.setFood(food);

        discountRepository.saveAndFlush(discount);

        DiscountDTO discountDTO = discountMapper.toDiscountDTO(discount);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(discountDTO);
    }

}
