package net.mymenu.controllers;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import net.mymenu.dto.banner.BannerRequest;
import net.mymenu.enums.banner.BannerRedirect;
import net.mymenu.exception.NotFoundException;
import net.mymenu.models.*;
import net.mymenu.models.Food;
import net.mymenu.repository.BannerRepository;
import net.mymenu.repository.CategoryRepository;
import net.mymenu.repository.FileStorageRepository;
import net.mymenu.repository.FoodRepository;
import net.mymenu.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/banner")
public class BannerController {

    @Autowired
    private BannerRepository bannerRepository;

    @Autowired
    private FileStorageRepository fileStorageRepository;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FoodRepository foodRepository;

    @GetMapping
    public ResponseEntity<Page<Banner>> list(Pageable pageable) {
        Company company = jwtHelper.extractUser().getCompany();
        Page<Banner> banners = bannerRepository.findAllByCompany(company, pageable);
        return ResponseEntity.ok(banners);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Banner> search(@PathVariable UUID id) {
        Company company = jwtHelper.extractUser().getCompany();
        Banner banner = bannerRepository.findByIdAndCompany(id, company);
        return ResponseEntity.ok(banner);
    }

    @PostMapping
    public Banner create(@RequestBody @Valid BannerRequest bannerRequest) {
        FileStorage image = fileStorageRepository.findById(bannerRequest.getImageId())
                .orElseThrow(() -> new NotFoundException("Image not found"));
        Company company = jwtHelper.extractUser().getCompany();

        Banner banner = Banner
                .builder()
                .title(bannerRequest.getTitle())
                .description(bannerRequest.getDescription())
                .image(image)
                .company(company)
                .url(bannerRequest.getUrl())
                .redirect(bannerRequest.getRedirect())
                .active(bannerRequest.getActive())
                .type(bannerRequest.getType())
                .build();

        setOptionalParams(bannerRequest, banner, company);

        return bannerRepository.save(banner);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Banner> update(@PathVariable UUID id, @RequestBody @Valid BannerRequest bannerRequest) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Banner not found"));
        FileStorage image = fileStorageRepository.findById(bannerRequest.getImageId())
                .orElseThrow(() -> new NotFoundException("Image not found"));
        Company company = jwtHelper.extractUser().getCompany();

        banner.setImage(image);
        banner.setCompany(company);
        banner.setUrl(bannerRequest.getUrl());
        banner.setRedirect(bannerRequest.getRedirect());
        banner.setActive(bannerRequest.getActive());

        setOptionalParams(bannerRequest, banner, company);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> remove(@PathVariable UUID id) {
        Company company = jwtHelper.extractUser().getCompany();
        bannerRepository.removeByIdAndCompany(id, company);
        return ResponseEntity.noContent().build();
    }

    private void setOptionalParams(@RequestBody @Valid BannerRequest bannerRequest, Banner banner, Company company) {
        if (bannerRequest.getRedirect() == null) {
            return;
        }

        if (bannerRequest.getRedirect().equals(BannerRedirect.CATEGORY)) {
            Category category = categoryRepository.findById(bannerRequest.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category not found"));
            banner.setCategory(category);
        }
        if (bannerRequest.getRedirect().equals(BannerRedirect.FOOD)) {
            Food food = foodRepository.findByIdAndCompanyId(bannerRequest.getFoodId(), company.getId())
                    .orElseThrow(() -> new NotFoundException("Food not found"));
            banner.setFood(food);
        }
    }
}
