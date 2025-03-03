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
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<MenuDTO> searchByCompanyId(@RequestHeader("_company") String companyUrl) {
        Company company = companyRepository.findByUrl(companyUrl)
                .orElseThrow(() -> new NotFoundException("Company not found"));

        List<Category> categories = categoryRepository.findAll();
        List<Banner> banners = bannerRepository.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(MenuDTO
                .builder()
                .banners(banners)
                .company(company)
                .categories(categories)
                .build());
    }
}
