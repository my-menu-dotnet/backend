package net.mymenu.home;

import net.mymenu.home.dto.HomeResponse;
import net.mymenu.company.CompanyRepository;
import net.mymenu.food.FoodRepository;
import net.mymenu.analytics.company_access.CompanyAccessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private CompanyAccessRepository companyAccessRepository;

    @GetMapping
    public ResponseEntity<HomeResponse> home() {
        long companies = companyRepository.count();
        long foods = foodRepository.count();
        long accesses = companyAccessRepository.count();

        return ResponseEntity.ok(new HomeResponse(companies, foods, accesses));
    }
}
