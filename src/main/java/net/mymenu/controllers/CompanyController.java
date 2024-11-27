package net.mymenu.controllers;

import net.mymenu.dto.AddressRequest;
import net.mymenu.dto.CompanyRequest;
import net.mymenu.mapper.CompanyMapper;
import net.mymenu.models.*;
import net.mymenu.repository.CategoryRepository;
import net.mymenu.repository.CompanyRepository;
import net.mymenu.repository.FileStorageRepository;
import net.mymenu.security.JwtHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    private CompanyMapper companyMapper;

    @Autowired
    private JwtHelper jwtHelper;

    @GetMapping
    public ResponseEntity<List<Company>> list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        List<Company> companies = companyRepository.findAll(pageable).getContent();

        String email = jwtHelper.extractEmail();
        if (email.equals(jwtHelper.ANONYMOUS)) {
            companies = companyMapper.toRestrictListCompany(companies);
        }

        return ResponseEntity.status(HttpStatus.OK).body(companies);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Company>> getCompany() {
        User user = jwtHelper.extractUser();
        return ResponseEntity.status(HttpStatus.OK).body(user.getCompanies());
    }

    @PostMapping
    public ResponseEntity<Company> registerCompany(@Valid @RequestBody CompanyRequest company) {
        User user = jwtHelper.extractUser();

        List<Category> categories = findCategories(company.getCategories());
        FileStorage image = findImage(company.getImageId());
        Address address = buildAddress(company.getAddress());

        Company newCompany = Company.builder()
                .name(company.getName())
                .cnpj(company.getCnpj())
                .email(company.getEmail())
                .phone(company.getPhone())
                .categories(categories)
                .image(image)
                .address(address)
                .build();

        user.getCompanies().add(newCompany);

        companyRepository.saveAndFlush(newCompany);

        return ResponseEntity.status(HttpStatus.CREATED).body(newCompany);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody CompanyRequest company, @PathVariable UUID id) {
        User user = jwtHelper.extractUser();

        Company companyToUpdate = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        if (!user.getCompanies().contains(companyToUpdate)) {
            throw new SecurityException("User does not have permission to update this company");
        }

        List<Category> categories = findCategories(company.getCategories());
        FileStorage image = findImage(company.getImageId());
        Address address = companyToUpdate.getAddress();

        if (address != null) {
            address.setStreet(company.getAddress().getStreet());
            address.setNumber(company.getAddress().getNumber());
            address.setComplement(company.getAddress().getComplement());
            address.setNeighborhood(company.getAddress().getNeighborhood());
            address.setCity(company.getAddress().getCity());
            address.setState(company.getAddress().getState());
            address.setZipCode(company.getAddress().getZipCode());
        } else {
            address = buildAddress(company.getAddress());
        }

        companyToUpdate.setName(company.getName());
        companyToUpdate.setCnpj(company.getCnpj());
        companyToUpdate.setEmail(company.getEmail());
        companyToUpdate.setPhone(company.getPhone());
        companyToUpdate.setCategories(categories);
        companyToUpdate.setImage(image);
        companyToUpdate.setAddress(address);

        companyRepository.saveAndFlush(companyToUpdate);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private List<Category> findCategories(List<UUID> categoryIds) {
        return categoryRepository.findAllByIdList(categoryIds)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    private FileStorage findImage(UUID imageId) {
        return fileStorageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));
    }

    private Address buildAddress(AddressRequest addressRequest) {
        return Address.builder()
                .street(addressRequest.getStreet())
                .number(addressRequest.getNumber())
                .complement(addressRequest.getComplement())
                .neighborhood(addressRequest.getNeighborhood())
                .city(addressRequest.getCity())
                .state(addressRequest.getState())
                .zipCode(addressRequest.getZipCode())
                .build();
    }
}
