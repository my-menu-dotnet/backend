package net.mymenu.category;

import jakarta.transaction.Transactional;
import net.mymenu.category.dto.CategoryOrder;
import net.mymenu.category.dto.CategoryRequest;
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
    private CategoryRepository categoryRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping
    public ResponseEntity<Page<Category>> find(Pageable pageable) {
        Page<Category> categoriesOrdered = categoryRepository.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(categoriesOrdered);
    }

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody CategoryRequest categoryRequest) {
        Category category = Category
                .builder()
                .name(categoryRequest.getName())
                .active(categoryRequest.isActive())
                .build();

        categoryRepository.saveAndFlush(category);

        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@RequestBody CategoryRequest categoryRequest, @PathVariable UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        category.setName(categoryRequest.getName());
        category.setActive(categoryRequest.isActive());

        categoryRepository.saveAndFlush(category);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{categoryId}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable UUID categoryId) {
        companyRepository.removeById(categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/order")
    public ResponseEntity<?> updateOrder(@RequestBody CategoryOrder categoryOrder) {
        List<Category> categories = categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "order"));

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
    public ResponseEntity<Map<String, String>> select() {
        List<Category> categories = categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "order"));

        Map<String, String> selectCategories = categories.parallelStream()
                .collect(HashMap::new, (m, c) -> m.put(c.getId().toString(), c.getName()), Map::putAll);

        return ResponseEntity.status(HttpStatus.OK).body(selectCategories);
    }
}
