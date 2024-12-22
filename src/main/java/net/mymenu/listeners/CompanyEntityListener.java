package net.mymenu.listeners;

import net.mymenu.interfaces.CompanyAware;
import net.mymenu.models.User;
import net.mymenu.security.JwtHelper;
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
        try {
            User user = jwtHelper.extractUser();

            if (entity.getCompany() == null && !isCompanyOwner(user, entity)) {
                throw new SecurityException("Company id does not match to the token");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new SecurityException("Company id does not match to the token");
        }
    }

    private boolean isCompanyOwner(User user, CompanyAware entity) {
        return user.getCompanies().contains(entity.getCompany());
    }
}
