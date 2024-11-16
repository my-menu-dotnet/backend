package com.digimenu._utils;

import com.digimenu.models.Company;
import com.digimenu.models.User;
import com.digimenu.repository.CompanyRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyTestHelper {

    public Company createCompanyIfNotExists(User user) {
        Company company = user.getCompanies().get(0);

//        if (company == null) {
//            CompanyRegister companyRegister = new CompanyRegister(
//                    faker.name().fullName(),
//                    faker.number().digits(14),
//                    faker.internet().emailAddress(),
//                    faker.phoneNumber().cellPhone(),
//            );
//
//            company = companyService.registerCompany(companyRegister, user);
//        }

        return company;
    }
}
