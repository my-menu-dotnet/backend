package com.digimenu.models;

import com.digimenu.security.JwtHelper;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public abstract class CompanyAware {
    abstract Company getCompany();

    @Autowired
    private JwtHelper jwtHelper;

    @PrePersist
    @PreUpdate
    public void validateCompanyId() {
        UUID companyId = jwtHelper.extractCompanyId();
        if (!this.getCompany().getId().equals(companyId)) {
            throw new SecurityException("Company id does not match to the token");
        }
    }
}
