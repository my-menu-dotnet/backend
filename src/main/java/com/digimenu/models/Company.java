package com.digimenu.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "company")
@Getter
@Setter
@NoArgsConstructor
public class Company {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "cnpj")
    private String cnpj;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @ManyToMany
    @JoinTable(
            name = "user_company",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore
    private List<User> users;

    public Company(String name, String cnpj, String phone, String email, User user) {
        this.name = name;
        this.cnpj = cnpj;
        this.phone = phone;
        this.email = email;
        addUser(user);
    }

    private void addUser(User user) {
        if (this.users != null) {
            this.users.add(user);
            return;
        }

        this.users = List.of(user);
    }
}
