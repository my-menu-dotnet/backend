package com.digimenu.models;

import com.digimenu.constraints.CPF;
import com.digimenu.constraints.FullName;
import com.digimenu.constraints.Phone;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    @FullName
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "cpf")
    @CPF
    private String cpf;

    @Column(name = "phone")
    @Phone
    private String phone;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @ManyToMany
    @JoinTable(
            name = "user_company",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "company_id")
    )
    private List<Company> companies;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @JsonIgnore
    private Role role;

    public User(String name, String email, String cpf, String phone, String password, Role role) {
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.phone = phone;
        this.password = password;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }

    @Override
    public String getUsername() {
        return email;
    }
}
