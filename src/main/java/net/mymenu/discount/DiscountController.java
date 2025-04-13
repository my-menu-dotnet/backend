package net.mymenu.discount;

import jakarta.validation.Valid;
import net.mymenu.discount.dto.DiscountDTO;
import net.mymenu.discount.dto.DiscountRequest;
import net.mymenu.exception.DuplicateException;
import net.mymenu.exception.NotFoundException;
import net.mymenu.food.Food;
import net.mymenu.food.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
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
    private DiscountMapper discountMapper;

    @GetMapping
    public ResponseEntity<List<DiscountDTO>> listDiscounts() {
        List<Discount> discounts = discountRepository.findAll();

        List<DiscountDTO> discountDTOS = discountMapper.toDiscountDTO(discounts)
                .stream()
                .sorted(Comparator.comparing(DiscountDTO::getStatus))
                .toList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(discountDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscountDTO> findDiscountById(@PathVariable UUID id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Discount not found"));

        DiscountDTO discountDTO = discountMapper.toDiscountDTO(discount);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(discountDTO);
    }

    @PostMapping
    public ResponseEntity<DiscountDTO> createDiscount(@RequestBody @Valid DiscountRequest discountRequest) {
        List<Food> foods = foodRepository.findAll();

        Food food = foods.stream()
                .filter(food1 -> food1.getId().equals(discountRequest.getFoodId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Food not found"));

        if (food.getActiveDiscount() != null && discountRequest.isActive()) {
            throw new DuplicateException("Discount already active");
        }

        Discount discount = Discount
                .builder()
                .type(discountRequest.getType())
                .startAt(discountRequest.getStartAt())
                .endAt(discountRequest.getEndAt())
                .discount(discountRequest.getDiscount())
                .active(discountRequest.isActive())
                .food(food)
                .build();

        validateDiscountOverlap(discount);
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

        if (food.getActiveDiscount() != null && discountRequest.isActive()) {
            throw new DuplicateException("Discount already active");
        }

        discount.setType(discountRequest.getType());
        discount.setStartAt(discountRequest.getStartAt());
        discount.setEndAt(discountRequest.getEndAt());
        discount.setDiscount(discountRequest.getDiscount());
        discount.setActive(discountRequest.isActive());
        discount.setFood(food);

        validateDiscountOverlap(discount);
        discountRepository.saveAndFlush(discount);

        DiscountDTO discountDTO = discountMapper.toDiscountDTO(discount);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(discountDTO);
    }

    private void validateDiscountOverlap(Discount discount) {
        boolean hasOverlap = discountRepository.hasOverlappingActiveDiscount(
                discount.getFood().getId(),
                discount.getStartAt(),
                discount.getEndAt(),
                discount.getId() != null ? discount.getId() : UUID.randomUUID()
        );

        if (hasOverlap) {
            throw new DuplicateException("There is already an active discount for this food in the specified date range");
        }
    }
}
