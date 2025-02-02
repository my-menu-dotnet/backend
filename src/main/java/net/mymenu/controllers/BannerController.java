package net.mymenu.controllers;

import jakarta.validation.Valid;
import net.mymenu.constraints.ValidDiscount;
import net.mymenu.dto.banner.BannerRequest;
import net.mymenu.exception.NotFoundException;
import net.mymenu.models.Banner;
import net.mymenu.models.Company;
import net.mymenu.models.FileStorage;
import net.mymenu.repository.BannerRepository;
import net.mymenu.repository.FileStorageRepository;
import net.mymenu.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping
    public List<Banner> list() {
        Company company = jwtHelper.extractUser().getCompany();
        return bannerRepository.findAllByCompany(company);
    }

    @PostMapping
    public Banner create(@RequestBody @Valid BannerRequest bannerRequest) {
        System.out.println(bannerRequest.getImageId());
        FileStorage image = fileStorageRepository.findById(bannerRequest.getImageId())
                .orElseThrow(() -> new NotFoundException("Image not found"));
        Company company = jwtHelper.extractUser().getCompany();

        Banner banner = Banner
                .builder()
                .image(image)
                .company(company)
                .url(bannerRequest.getUrl())
                .redirect(bannerRequest.getRedirect())
                .active(bannerRequest.getActive())
                .build();

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

        return ResponseEntity.noContent().build();
    }
}
