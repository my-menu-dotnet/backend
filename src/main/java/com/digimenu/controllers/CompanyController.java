package com.digimenu.controllers;

import com.digimenu.dto.CompanyCreate;
import com.digimenu.models.Category;
import com.digimenu.models.Company;
import com.digimenu.models.FileStorage;
import com.digimenu.models.User;
import com.digimenu.repository.CategoryRepository;
import com.digimenu.repository.CompanyRepository;
import com.digimenu.repository.FileStorageRepository;
import com.digimenu.security.JwtHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FileStorageRepository fileStorageRepository;

    @Autowired
    private JwtHelper jwtHelper;

    @GetMapping
    public ResponseEntity<List<Company>> list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("created_at").ascending());
        List<Company> companies = companyRepository.findAll(pageable).getContent();
        return ResponseEntity.status(HttpStatus.OK).body(companies);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Company>> getCompany() {
        User user = jwtHelper.extractUser();
        return ResponseEntity.status(HttpStatus.OK).body(user.getCompanies());
    }

    @PostMapping
    public ResponseEntity<Company> registerCompany(@Valid @RequestBody CompanyCreate company) {
        User user = jwtHelper.extractUser();

        List<Category> categories = categoryRepository.findAllByIdList(company.getCategories())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        FileStorage image = fileStorageRepository.findById(company.getImageId())
                .orElseThrow(() -> new RuntimeException("Image not found"));

        Company newCompany = Company.builder()
                .name(company.getName())
                .cnpj(company.getCnpj())
                .email(company.getEmail())
                .phone(company.getPhone())
                .categories(categories)
                .image(image)
                .build();

        user.getCompanies().add(newCompany);

        companyRepository.saveAndFlush(newCompany);

        return ResponseEntity.status(HttpStatus.CREATED).body(newCompany);
    }
}
