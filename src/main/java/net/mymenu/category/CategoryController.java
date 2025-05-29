package net.mymenu.category;

import jakarta.transaction.Transactional;
import net.mymenu.category.dto.CategoryActiveRequest;
import net.mymenu.category.dto.CategoryOrder;
import net.mymenu.category.dto.CategoryRequest;
import net.mymenu.category.dto.CategoryResponse;
import net.mymenu.exception.NotFoundException;
import net.mymenu.company.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/category")
@PreAuthorize("hasRole('ADMIN')")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryMapper categoryMapper;

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findById(@PathVariable UUID id) {
        Category category = categoryService.findById(id);
        CategoryResponse response = categoryMapper.toCategoryResponse(category);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    public ResponseEntity<Page<CategoryResponse>> find(Pageable pageable) {
        Page<Category> categoriesOrdered = categoryService.findAllPageable(pageable);
        Page<CategoryResponse> responses = categoriesOrdered.map(categoryMapper::toCategoryResponse);
        return ResponseEntity.ok().body(responses);
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@RequestBody CategoryRequest categoryRequest) {
        Category category = categoryService.createCategoryByRequest(categoryRequest);
        CategoryResponse response = categoryMapper.toCategoryResponse(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody CategoryRequest categoryRequest, @PathVariable UUID id) {
        categoryService.updateCategoryByRequest(id, categoryRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{categoryId}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable UUID categoryId) {
        categoryService.deleteCategoryById(categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/order")
    public ResponseEntity<?> updateOrder(@RequestBody CategoryOrder categoryOrder) {
        categoryService.updateCategoryOrder(categoryOrder);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/active/{id}")
    public ResponseEntity<?> updateActive(
            @PathVariable UUID id,
            @RequestBody CategoryActiveRequest request) {
        categoryService.updateCategoryActive(id, request.isActive());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/select")
    public ResponseEntity<Map<String, String>> select(Pageable pageable) {
        Map<String, String> selectCategories = categoryService.findSelectCategories(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(selectCategories);
    }
}
