package com.digimenu.models;

import com.digimenu.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public class Role implements GrantedAuthority {

    private UserRole name;

    @Override
    public String getAuthority() {
        return "ROLE_" + name.name();
    }
}
