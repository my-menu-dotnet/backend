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
import net.mymenu.service.CompanyService;
import net.mymenu.service.QRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private CompanyService companyService;

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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Company>> getCompany() {
        User user = jwtHelper.extractUser();
        return ResponseEntity.status(HttpStatus.OK).body(user.getCompanies());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Company> registerCompany(@Valid @RequestBody CompanyRequest company) {
        User user = jwtHelper.extractUser();

        List<Category> categories = companyService.findCategories(company.getCategories());
        FileStorage image = companyService.findImage(company.getImageId());
        Address address = companyService.buildAddress(company.getAddress());

        FileStorage header = null;
        if (company.getHeaderId() != null) {
            header = companyService.findImage(company.getHeaderId());
        }

        Company newCompany = Company.builder()
                .name(company.getName())
                .cnpj(company.getCnpj())
                .email(company.getEmail())
                .primaryColor(company.getPrimaryColor())
                .phone(company.getPhone())
                .categories(categories)
                .image(image)
                .header(header)
                .address(address)
                .url(UUID.randomUUID().toString())
                .build();

        user.getCompanies().add(newCompany);

        companyRepository.saveAndFlush(newCompany);

        return ResponseEntity.status(HttpStatus.CREATED).body(newCompany);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody CompanyRequest company, @PathVariable UUID id) {
        User user = jwtHelper.extractUser();

        Company companyToUpdate = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        if (!user.getCompanies().contains(companyToUpdate)) {
            throw new SecurityException("User does not have permission to update this company");
        }

        FileStorage image = companyService.findImage(company.getImageId());
        Address address = companyToUpdate.getAddress();

        FileStorage header = companyToUpdate.getHeader();
        if (company.getHeaderId() != null) {
            header = companyService.findImage(company.getHeaderId());
        }

        if (address != null) {
            address.setStreet(company.getAddress().getStreet());
            address.setNumber(company.getAddress().getNumber());
            address.setComplement(company.getAddress().getComplement());
            address.setNeighborhood(company.getAddress().getNeighborhood());
            address.setCity(company.getAddress().getCity());
            address.setState(company.getAddress().getState());
            address.setZipCode(company.getAddress().getZipCode());
        } else {
            address = companyService.buildAddress(company.getAddress());
        }

        companyToUpdate.setName(company.getName());
        companyToUpdate.setCnpj(company.getCnpj());
        companyToUpdate.setEmail(company.getEmail());
        companyToUpdate.setPrimaryColor(company.getPrimaryColor());
        companyToUpdate.setPhone(company.getPhone());
        companyToUpdate.setImage(image);
        companyToUpdate.setHeader(header);
        companyToUpdate.setAddress(address);

        companyRepository.saveAndFlush(companyToUpdate);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/qr-code")
    public ResponseEntity<byte[]> getCompanyQrCode() {
        User user = jwtHelper.extractUser();
        Company company = user.getCompanies().getFirst();

        byte[] qrCode = QRCodeService.generateQRCodeImage(company.getId().toString());

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Content-Type", "image/png")
                .body(qrCode);
    }
}
