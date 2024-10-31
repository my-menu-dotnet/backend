package com.digimenu._utils;

import com.digimenu.dto.CompanyRegister;
import com.digimenu.models.Company;
import com.digimenu.models.User;
import com.digimenu.repository.CompanyRepository;
import com.digimenu.service.CompanyService;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyTestHelper {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyService companyService;

    private final Faker faker = new Faker();

    public Company createCompanyIfNotExists(User user) {
        Company company = companyRepository.findAllByUser(user.getId())
                .orElse(List.of())
                .stream().findFirst()
                .orElse(null);

        if (company == null) {
            CompanyRegister companyRegister = new CompanyRegister(
                    faker.name().fullName(),
                    faker.number().digits(14),
                    faker.internet().emailAddress(),
                    faker.phoneNumber().cellPhone()
            );

            company = companyService.registerCompany(companyRegister, user);
        }

        return company;
    }
}
