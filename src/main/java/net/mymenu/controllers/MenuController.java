package net.mymenu.controllers;

import net.mymenu.dto.menu.MenuDTO;
import net.mymenu.exception.NotFoundException;
import net.mymenu.models.Banner;
import net.mymenu.models.Category;
import net.mymenu.models.Company;
import net.mymenu.repository.BannerRepository;
import net.mymenu.repository.CategoryRepository;
import net.mymenu.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BannerRepository bannerRepository;

    @GetMapping("/{companyId}")
    public ResponseEntity<MenuDTO> searchByCompanyId(@PathVariable String companyId) {
        UUID id = null;

        try {
            id = UUID.fromString(companyId);
        } catch (IllegalArgumentException _) {
        }

        Company company = companyRepository.findByIdOrUrl(id, companyId)
                .orElseThrow(() -> new NotFoundException("Company not found"));

        List<Category> categories = categoryRepository.findAllByCompanyId(company.getId())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        List<Banner> banners = bannerRepository.findAllByCompanyAndActiveIsTrue(company);

        return ResponseEntity.status(HttpStatus.OK).body(MenuDTO
                .builder()
                .banners(banners)
                .company(company)
                .categories(categories)
                .build());
    }
}
