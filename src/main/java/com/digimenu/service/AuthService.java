package com.digimenu.service;

import com.digimenu.dto.auth.AuthRegister;
import com.digimenu.exception.DuplicateException;
import com.digimenu.exception.NotFoundException;
import com.digimenu.models.Role;
import com.digimenu.models.User;
import com.digimenu.repository.RoleRepository;
import com.digimenu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(AuthRegister authRegister) {
        userRepository.findByEmailOrCpf(authRegister.getEmail(), authRegister.getCpf())
                .ifPresent(_ -> {
                    throw new DuplicateException("Email or CPF already registered");
                });

        Role adminRole = roleRepository.findByName("ADMIN");

        User user = new User(
                authRegister.getName(),
                authRegister.getEmail(),
                authRegister.getCpf(),
                authRegister.getPhone(),
                passwordEncoder.encode(authRegister.getPassword()),
                adminRole
        );

        userRepository.save(user);

        return user;
    }

    public void validateUserHasCompany(User user, UUID companyId) {
        boolean hasCompany = user.getCompanies()
                .stream()
                .anyMatch(company -> company.getId().equals(companyId));
        if (!hasCompany) {
            throw new NotFoundException("Company not found");
        }
    }
}
