package com.digimenu.models;

import com.digimenu.constraints.CPF;
import com.digimenu.constraints.FullName;
import com.digimenu.constraints.Phone;
import com.digimenu.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
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
@AllArgsConstructor
@Builder
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

    @OneToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_company",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "company_id")
    )
    @JsonIgnore
    private List<Company> companies;

    public User(String name, String email, String cpf, String phone, String password) {
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.phone = phone;
        this.password = password;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.companies == null || this.companies.isEmpty()) {
            return List.of(new Role());
        }
        return List.of(new Role(UserRole.ADMIN));
    }

    @Override
    public String getUsername() {
        return email;
    }
}
