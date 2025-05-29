package net.mymenu.category;

import net.mymenu.category.dto.CategoryOrder;
import net.mymenu.category.dto.CategoryRequest;
import net.mymenu.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Page<Category> findAllPageable(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    public Category findById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
    }

    public Category createCategoryByRequest(CategoryRequest request) {
        Category category = mapToCategory(new Category(), request);

        Category lastCategory = gatLastCategory();
        if (lastCategory != null) {
            category.setOrder(lastCategory.getOrder() + 1);
        } else {
            category.setOrder(0);
        }

        return categoryRepository.save(category);
    }

    public Category updateCategoryByRequest(UUID id, CategoryRequest request) {
        Category category = mapToCategory(findById(id), request);
        return categoryRepository.save(category);
    }

    public void deleteCategoryById(UUID id) {
        categoryRepository.deleteById(id);
    }

    public void updateCategoryOrder(CategoryOrder categoryOrder) {
        List<Category> categories = categoryRepository.findAllById(categoryOrder.getIds());

        if (categories.size() != categoryOrder.getIds().size()) {
            throw new NotFoundException("One or more categories not found");
        }

        Map<UUID, Integer> orderMap = new HashMap<>();
        int i = 0;
        for (UUID id : categoryOrder.getIds()) {
            orderMap.put(id, i++);
        }

        categories.forEach(category -> category.setOrder(orderMap.get(category.getId())));

        categoryRepository.saveAll(categories);
    }

    public void updateCategoryActive(UUID id, boolean active) {
        Category category = findById(id);
        category.setActive(active);
        categoryRepository.save(category);
    }

    public Map<String, String> findSelectCategories(Pageable pageable) {
        List<Category> categories = categoryRepository.findAll(pageable).getContent();
        return categories.parallelStream()
                .collect(HashMap::new, (m, c) -> m.put(c.getId().toString(), c.getName()), Map::putAll);
    }

    private Category mapToCategory(Category category, CategoryRequest request) {
        category.setName(request.getName());
        category.setActive(request.isActive());
        return category;
    }

    private Category gatLastCategory() {
        return categoryRepository.findLastCategoryOrder();
    }
}
