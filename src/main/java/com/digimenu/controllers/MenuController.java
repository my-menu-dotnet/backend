package com.digimenu.controllers;

import com.digimenu.dto.menu.MenuCategoryDTO;
import com.digimenu.dto.menu.MenuDTO;
import com.digimenu.exception.NotFoundException;
import com.digimenu.models.Category;
import com.digimenu.models.Company;
import com.digimenu.models.Food;
import com.digimenu.repository.CompanyRepository;
import com.digimenu.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private FoodRepository foodRepository;

    @GetMapping("/{companyId}")
    public ResponseEntity<MenuDTO> searchByCompanyId(@PathVariable UUID companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Company not found"));

        // Get all foods of the company grouped by category
        List<Food> food = foodRepository.findByCompanyId(companyId)
                .orElseThrow(() -> new NotFoundException("Food not found"));

        List<MenuCategoryDTO> categories = food.parallelStream()
                .collect(Collectors.groupingBy(Food::getCategory))
                .entrySet().parallelStream()
                .map(entry -> MenuCategoryDTO
                        .builder()
                        .id(entry.getKey().getId())
                        .name(entry.getKey().getName())
                        .description(entry.getKey().getDescription())
                        .image(entry.getKey().getImage())
                        .status(entry.getKey().getStatus())
                        .food(entry.getValue())
                        .build())
                .toList();

        MenuDTO menuDTO = MenuDTO
                .builder()
                .company(company)
                .categories(categories)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(menuDTO);
    }
}
