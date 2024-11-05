package com.digimenu.controllers;

import com.digimenu.dto.category.CategoryCreate;
import com.digimenu.mapper.CategoryMapper;
import com.digimenu.models.Category;
import com.digimenu.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<Category[]> list() {
        Category[] categories = categoryService.findAll();
        Category[] categoryResponse = categoryMapper.toCategory(categories);

        return ResponseEntity.status(HttpStatus.OK).body(categoryResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> find(@PathVariable UUID id) {
        Category category = categoryService.findById(id);
        Category categoryResponse = categoryMapper.toCategoryResponse(category);

        return ResponseEntity.status(HttpStatus.OK).body(categoryResponse);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> create(@RequestBody CategoryCreate categoryCreate) {
        Category category = categoryService.create(categoryCreate);
        Category categoryResponse = categoryMapper.toCategoryResponse(category);

        return ResponseEntity.status(HttpStatus.CREATED).body(categoryResponse);
    }
}
