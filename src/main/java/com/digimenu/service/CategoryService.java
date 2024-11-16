package com.digimenu.service;

import com.digimenu.dto.category.CategoryCreate;
import com.digimenu.exception.NotFoundException;
import com.digimenu.models.Category;
import com.digimenu.models.Company;
import com.digimenu.models.FileStorage;
import com.digimenu.repository.CategoryRepository;
import com.digimenu.repository.CompanyRepository;
import com.digimenu.repository.FileStorageRepository;
import com.digimenu.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private FileStorageRepository fileStorageRepository;

    @Autowired
    private JwtHelper jwtHelper;

    @Deprecated
    public Category[] findAll() {
        List<Category> companies = categoryRepository.findAll();

        if (companies.isEmpty()) {
            throw new NotFoundException("Category not found");
        }

        return companies.toArray(new Category[0]);
    }

    public Category findById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
    }

    public Category create(CategoryCreate categoryCreate) {
        Company company = companyRepository.findById(categoryCreate.getCompanyId())
                .orElseThrow(() -> new NotFoundException("Company not found"));
        FileStorage image = fileStorageRepository.findById(categoryCreate.getImageId())
                .orElse(null);

        Category category = new Category(
                categoryCreate.getName(),
                categoryCreate.getDescription(),
                image,
                categoryCreate.getStatus(),
                company
        );

        return categoryRepository.saveAndFlush(category);
    }
}
