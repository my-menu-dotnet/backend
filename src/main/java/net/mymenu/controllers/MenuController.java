package net.mymenu.controllers;

import net.mymenu.dto.CompanyResponse;
import net.mymenu.dto.menu.MenuDTO;
import net.mymenu.enums.CategoryStatus;
import net.mymenu.exception.NotFoundException;
import net.mymenu.mapper.CompanyMapper;
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

    @Autowired
    private CompanyMapper companyMapper;

    @GetMapping
    public ResponseEntity<MenuDTO> searchByCompanyId(@RequestHeader("X-Company-ID") String companyUrl) {
        Company company = companyRepository.findByUrl(companyUrl)
                .orElseThrow(() -> new NotFoundException("Company not found"));

        List<Category> categories = categoryRepository.findAllByStatusOrderByOrder(CategoryStatus.ACTIVE);
        List<Banner> banners = bannerRepository.findAllByActiveTrue();

        CompanyResponse companyResponse = companyMapper.toCompanyResponse(company);

        return ResponseEntity.status(HttpStatus.OK).body(MenuDTO
                .builder()
                .banners(banners)
                .company(companyResponse)
                .categories(categories)
                .build());
    }
}


