package net.mymenu.repository;

import net.mymenu.category.CategoryRepository;
import net.mymenu.config.TenantTest;
import net.mymenu.category.enums.CategoryStatus;
import net.mymenu.category.Category;
import org.springframework.beans.factory.annotation.Autowired;

class CategoryRepositoryTest extends TenantTest<Category> {

    @Autowired
    protected CategoryRepository repository;

    @Override
    protected Category getEntity() {
        return Category.builder()
                .name("Category 1")
                .active(true)
                .build();
    }

    @Override
    protected CategoryRepository getRepository() {
        return repository;
    }
}