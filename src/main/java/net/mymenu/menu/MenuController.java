package net.mymenu.menu;

import net.mymenu.menu.dto.MenuDTO;
import net.mymenu.exception.NotFoundException;
import net.mymenu.banner.Banner;
import net.mymenu.category.Category;
import net.mymenu.company.Company;
import net.mymenu.banner.BannerRepository;
import net.mymenu.category.CategoryRepository;
import net.mymenu.company.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<MenuDTO> searchByCompanyId(@RequestHeader("X-Company-ID") String companyUrl) {
        Company company = companyRepository.findByUrl(companyUrl)
                .orElseThrow(() -> new NotFoundException("Company not found"));

        List<Category> categories = categoryRepository.findAllByActiveTrueOrderByOrder();
        List<Banner> banners = bannerRepository.findAllByActiveTrue();

        return ResponseEntity.status(HttpStatus.OK).body(MenuDTO
                .builder()
                .banners(banners)
                .company(company)
                .categories(categories)
                .build());
    }
}
