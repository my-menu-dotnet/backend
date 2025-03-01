package net.mymenu.repository;

import net.mymenu.config.TenantTest;
import net.mymenu.enums.CategoryStatus;
import net.mymenu.models.Category;
import net.mymenu.tenant.TenantContext;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CategoryRepositoryTest extends TenantTest<Category> {

    @Autowired
    protected CategoryRepository repository;

    @Override
    protected Category getEntity() {
        return Category.builder()
                .name("Category 1")
                .status(CategoryStatus.ACTIVE)
                .build();
    }

    @Override
    protected CategoryRepository getRepository() {
        return repository;
    }
}