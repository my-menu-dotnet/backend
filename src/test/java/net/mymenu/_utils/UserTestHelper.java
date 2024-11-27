package net.mymenu._utils;

import net.mymenu.dto.auth.AuthRegister;
import net.mymenu.models.User;
import net.mymenu.repository.UserRepository;
import net.mymenu.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserTestHelper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    final String NAME = "Test User";
    final String EMAIL = "teste@teste.com";
    final String CPF = "90828187010";
    final String PHONE = "11999999999";
    final String PASSWORD = "123456";

    public User createUserIfNotExists() {
        User user = userRepository.findByEmail(EMAIL).orElse(null);

        if (user == null) {
            AuthRegister authRegister = new AuthRegister(NAME, EMAIL, CPF, PHONE, PASSWORD);
            user = authService.registerUser(authRegister);
        }

        user.setPassword(PASSWORD);
        return user;
    }
}
