package com.digimenu._utils;

import com.digimenu.dto.AuthRegister;
import com.digimenu.models.User;
import com.digimenu.repository.UserRepository;
import com.digimenu.service.AuthService;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserTestHelper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    private final Faker faker = new Faker();
    final String NAME = "Test User";
    final String EMAIL = "teste@teste.com";
    final String PASSWORD = "123456";

    public User createUserIfNotExists() {
        User user = userRepository.findByEmail(EMAIL)
                .orElse(null);

        if (user == null) {
            AuthRegister authRegister = new AuthRegister(
                    NAME,
                    EMAIL,
                    faker.number().digits(10),
                    null,
                    PASSWORD
            );

            user = authService.registerUser(authRegister);
        }

        user.setPassword(PASSWORD);
        return user;
    }
}
