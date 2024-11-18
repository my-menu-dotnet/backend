package com.digimenu.listeners;

import com.digimenu.interfaces.CompanyAware;
import com.digimenu.models.User;
import com.digimenu.security.JwtHelper;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class CompanyEntityListener {

    @Lazy
    @Autowired
    private JwtHelper jwtHelper;

    @PrePersist
    @PreUpdate
    public void validateCompanyId(CompanyAware entity) {
        User user = jwtHelper.extractUser();

        if (entity.getCompany() == null && !isCompanyOwner(user, entity)) {
            throw new SecurityException("Company id does not match to the token");
        }
    }

    private boolean isCompanyOwner(User user, CompanyAware entity) {
        return user.getCompanies().contains(entity.getCompany());
    }
}
