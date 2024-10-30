package com.digimenu.service;

import com.digimenu.dto.CompanyRegister;
import com.digimenu.models.Company;
import com.digimenu.models.User;
import com.digimenu.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public Company registerCompany(CompanyRegister company, User user) {
        Company newCompany = new Company(
                company.getName(),
                company.getCnpj(),
                company.getEmail(),
                company.getPhone(),
                user
        );
        companyRepository.saveAndFlush(newCompany);
        return newCompany;
    }
}
