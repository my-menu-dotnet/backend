package com.digimenu.controllers;

import com.digimenu.dto.CompanyRegister;
import com.digimenu.models.Company;
import com.digimenu.models.User;
import com.digimenu.security.JwtHelper;
import com.digimenu.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private JwtHelper jwtHelper;

    @PostMapping("/register")
    public ResponseEntity<Company> registerCompany(@Valid @RequestBody CompanyRegister companyRegister) {
        User user = jwtHelper.extractUser();
        Company company = companyService.registerCompany(companyRegister, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(company);
    }

    @GetMapping
    public ResponseEntity<List<Company>> getCompany() {
        User user = jwtHelper.extractUser();
        return ResponseEntity.status(HttpStatus.OK).body(user.getCompanies());
    }
}
