package com.digimenu.listeners;

import com.digimenu.models.CompanyAware;
import com.digimenu.security.JwtHelper;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.util.UUID;

public class CompanyEntityListener {

    @PrePersist
    @PreUpdate
    public void validateCompanyId(CompanyAware entity) {
        JwtHelper jwtHelper = new JwtHelper();
        UUID companyId = jwtHelper.extractCompanyId();

        if (!entity.getCompany().getId().equals(companyId)) {
            throw new SecurityException("Company id does not match to the token");
        }
    }
}
