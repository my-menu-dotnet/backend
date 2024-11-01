package com.digimenu.service;

import com.digimenu.dto.category.CategoryCreate;
import com.digimenu.exception.NotFoundException;
import com.digimenu.models.Category;
import com.digimenu.models.Company;
import com.digimenu.repository.CategoryRepository;
import com.digimenu.repository.CompanyRepository;
import com.digimenu.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JwtHelper jwtHelper;

    public Category create(CategoryCreate categoryCreate) {
        UUID companyId = jwtHelper.extractCompanyId();
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Company not found"));

        Category category = new Category(
                categoryCreate.getName(),
                categoryCreate.getDescription(),
                categoryCreate.getImage(),
                categoryCreate.getStatus(),
                company
        );

        return categoryRepository.saveAndFlush(category);
    }
}
