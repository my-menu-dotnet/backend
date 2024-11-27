package net.mymenu._utils;

import net.mymenu.models.Company;
import net.mymenu.models.User;
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
