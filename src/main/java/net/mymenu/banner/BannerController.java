package net.mymenu.banner;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import net.mymenu.category.Category;
import net.mymenu.banner.dto.BannerRequest;
import net.mymenu.banner.enums.BannerRedirect;
import net.mymenu.exception.NotFoundException;
import net.mymenu.file_storage.FileStorage;
import net.mymenu.food.Food;
import net.mymenu.category.CategoryRepository;
import net.mymenu.file_storage.FileStorageRepository;
import net.mymenu.food.FoodRepository;
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
    private CategoryRepository categoryRepository;

    @Autowired
    private FoodRepository foodRepository;

    @GetMapping
    public ResponseEntity<Page<Banner>> list(Pageable pageable) {
        Page<Banner> banners = bannerRepository.findAll(pageable);
        return ResponseEntity.ok(banners);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Banner> search(@PathVariable UUID id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Banner not found"));
        return ResponseEntity.ok(banner);
    }

    @PostMapping
    public Banner create(@RequestBody @Valid BannerRequest bannerRequest) {
        FileStorage image = fileStorageRepository.findById(bannerRequest.getImageId())
                .orElseThrow(() -> new NotFoundException("Image not found"));

        Banner banner = Banner
                .builder()
                .title(bannerRequest.getTitle())
                .description(bannerRequest.getDescription())
                .image(image)
                .url(bannerRequest.getUrl())
                .redirect(bannerRequest.getRedirect())
                .active(bannerRequest.getActive())
                .type(bannerRequest.getType())
                .build();

        setOptionalParams(bannerRequest, banner);

        return bannerRepository.save(banner);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Banner> update(@PathVariable UUID id, @RequestBody @Valid BannerRequest bannerRequest) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Banner not found"));
        FileStorage image = fileStorageRepository.findById(bannerRequest.getImageId())
                .orElseThrow(() -> new NotFoundException("Image not found"));

        banner.setImage(image);
        banner.setUrl(bannerRequest.getUrl());
        banner.setRedirect(bannerRequest.getRedirect());
        banner.setActive(bannerRequest.getActive());

        setOptionalParams(bannerRequest, banner);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> remove(@PathVariable UUID id) {
        bannerRepository.removeById(id);
        return ResponseEntity.noContent().build();
    }

    private void setOptionalParams(@RequestBody @Valid BannerRequest bannerRequest, Banner banner) {
        if (bannerRequest.getRedirect() == null) {
            return;
        }

        if (bannerRequest.getRedirect().equals(BannerRedirect.CATEGORY)) {
            Category category = categoryRepository.findById(bannerRequest.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category not found"));
            banner.setCategory(category);
        }
        if (bannerRequest.getRedirect().equals(BannerRedirect.FOOD)) {
            Food food = foodRepository.findById(bannerRequest.getFoodId())
                    .orElseThrow(() -> new NotFoundException("Food not found"));
            banner.setFood(food);
        }
    }
}
