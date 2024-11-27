package net.mymenu.controllers;

import net.mymenu.dto.category.CategoryRequest;
import net.mymenu.exception.NotFoundException;
import net.mymenu.models.Category;
import net.mymenu.models.FileStorage;
import net.mymenu.repository.CategoryRepository;
import net.mymenu.repository.FileStorageRepository;
import net.mymenu.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FileStorageRepository fileStorageRepository;

    @GetMapping
    public ResponseEntity<List<Category>> list() {
        List<Category> companies = categoryRepository.findAll();

        if (companies.isEmpty()) {
            throw new NotFoundException("Category not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(companies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> find(@PathVariable UUID id) {
        Category category = categoryService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(category);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> create(@RequestBody CategoryRequest categoryRequest) {
        FileStorage image = null;

        if (categoryRequest.getImageId() != null) {
            image = fileStorageRepository.findById(categoryRequest.getImageId())
                    .orElseThrow(() -> new NotFoundException("Image not found"));
        }

        Category category = Category
                .builder()
                .name(categoryRequest.getName())
                .description(categoryRequest.getDescription())
                .image(image)
                .status(categoryRequest.getStatus())
                .build();

        categoryRepository.saveAndFlush(category);

        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> update(@RequestBody CategoryRequest categoryRequest, @PathVariable UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        FileStorage image = null;

        if (categoryRequest.getImageId() != null) {
            image = fileStorageRepository.findById(categoryRequest.getImageId())
                    .orElseThrow(() -> new NotFoundException("Image not found"));
        }

        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        category.setImage(image);
        category.setStatus(categoryRequest.getStatus());

        categoryRepository.saveAndFlush(category);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
