package com.digimenu.service;

import com.digimenu.exception.NotFoundException;
import com.digimenu.models.Category;
import com.digimenu.repository.CategoryRepository;
import com.digimenu.repository.CompanyRepository;
import com.digimenu.repository.FileStorageRepository;
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
    private FileStorageRepository fileStorageRepository;

    @Autowired
    private JwtHelper jwtHelper;

    public Category findById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
    }
}
