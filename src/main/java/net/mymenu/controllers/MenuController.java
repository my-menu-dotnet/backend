package net.mymenu.controllers;

import lombok.Getter;
import net.mymenu.dto.menu.MenuDTO;
import net.mymenu.enums.analytics.AccessWays;
import net.mymenu.exception.NotFoundException;
import net.mymenu.models.Category;
import net.mymenu.models.Company;
import net.mymenu.repository.CategoryRepository;
import net.mymenu.repository.CompanyRepository;
import net.mymenu.service.analytics.CompanyAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyAccessService companyAccessService;

    @GetMapping("/{companyUrl}/url")
    public ResponseEntity<MenuDTO> searchByCompanyUrl(@PathVariable String companyUrl) {
        Company company = companyRepository.findByUrl(companyUrl)
                .orElseThrow(() -> new NotFoundException("Company not found"));

        companyAccessService.logCompanyAccess(AccessWays.WEB, company.getId());

        MenuDTO menuDTO = MenuDTO
                .builder()
                .company(company)
                .categories(company.getCategories())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(menuDTO);
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<MenuDTO> searchByCompanyId(@PathVariable UUID companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Company not found"));

        companyAccessService.logCompanyAccess(AccessWays.QR_CODE, company.getId());

        MenuDTO menuDTO = MenuDTO
                .builder()
                .company(company)
                .categories(company.getCategories())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(menuDTO);
    }

}
