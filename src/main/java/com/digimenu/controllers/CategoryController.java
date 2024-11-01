package com.digimenu.controllers;

import com.digimenu.dto.category.CategoryCreate;
import com.digimenu.dto.category.CategoryResponse;
import com.digimenu.mapper.CategoryMapper;
import com.digimenu.models.Category;
import com.digimenu.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryMapper categoryMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> create(@RequestBody CategoryCreate categoryCreate) {
        Category category = categoryService.create(categoryCreate);
        CategoryResponse categoryResponse = categoryMapper.toCategoryResponse(category);

        return ResponseEntity.status(HttpStatus.CREATED).body(categoryResponse);
    }
}
