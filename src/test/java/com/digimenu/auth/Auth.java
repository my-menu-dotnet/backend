package com.digimenu.auth;

import com.digimenu.models.Company;
import com.digimenu.models.User;
import com.github.javafaker.Faker;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

public class Auth {
    final Faker faker = new Faker();
    final Gson gson = new Gson();

    static User user;
    static Company company;

    static String name;
    static String email;
    static String cpf;
    static String password;

    public Auth() {
        if (name == null) {
            name = faker.name().fullName();
        }
        if (email == null) {
            email = faker.internet().emailAddress();
        }
        if (cpf == null) {
            cpf = faker.number().digits(11);
        }
        if (password == null) {
            password = faker.internet().password(8, 20, true, true, true);
        }
    }
}
