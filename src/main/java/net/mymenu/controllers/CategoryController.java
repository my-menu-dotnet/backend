package net.mymenu.controllers;

import jakarta.transaction.Transactional;
import net.mymenu.dto.category.CategoryOrder;
import net.mymenu.dto.category.CategoryRequest;
import net.mymenu.exception.NotFoundException;
import net.mymenu.models.Category;
import net.mymenu.models.Company;
import net.mymenu.models.User;
import net.mymenu.repository.CategoryRepository;
import net.mymenu.repository.CompanyRepository;
import net.mymenu.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JwtHelper jwtHelper;

    @GetMapping("/me")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Category>> find() {
        User user = jwtHelper.extractUser();

        List<Category> categoriesOrdered = user
                .getCompanies()
                .getFirst()
                .getCategories()
                .parallelStream()
                .sorted(Comparator.comparingInt(Category::getOrder))
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(categoriesOrdered);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> create(@RequestBody CategoryRequest categoryRequest) {
        User user = jwtHelper.extractUser();

        Category category = Category
                .builder()
                .name(categoryRequest.getName())
                .status(categoryRequest.getStatus())
                .company(user.getCompanies().getFirst())
                .build();

        categoryRepository.saveAndFlush(category);

        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> update(@RequestBody CategoryRequest categoryRequest, @PathVariable UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        category.setName(categoryRequest.getName());
        category.setStatus(categoryRequest.getStatus());

        categoryRepository.saveAndFlush(category);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable UUID categoryId) {
        User user = jwtHelper.extractUser();

        Company company = user.getCompanies().getFirst();
        company.getCategories().removeIf(c -> c.getId().equals(categoryId));

        companyRepository.save(company);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/order")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateOrder(@RequestBody CategoryOrder categoryOrder) {
        User user = jwtHelper.extractUser();

        List<Category> categories = user.getCompanies().getFirst().getCategories();

        int i = 0;
        for (UUID order : categoryOrder.getIds()) {
            Category category = categories.stream()
                    .filter(c -> c.getId().equals(order))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Category not found"));

            category.setOrder(i);
            i++;
        }

        categoryRepository.saveAll(categories);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/select")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> select() {
        User user = jwtHelper.extractUser();

        Company company = user.getCompanies().getFirst();

        Map<String, String> categories = company.getCategories().parallelStream()
                .collect(HashMap::new, (m, c) -> m.put(c.getId().toString(), c.getName()), Map::putAll);

        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }
}
