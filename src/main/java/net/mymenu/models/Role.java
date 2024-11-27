package net.mymenu.models;

import net.mymenu.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
@NoArgsConstructor
public class Role implements GrantedAuthority {

    private UserRole name;

    @Override
    public String getAuthority() {
        return "ROLE_" + name.name();
    }
}
