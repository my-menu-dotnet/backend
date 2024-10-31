package com.digimenu.auth;

import com.digimenu.dto.AuthRegister;
import com.digimenu.models.User;
import com.github.javafaker.Faker;
import com.google.gson.Gson;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RegisterTest {

    @Autowired
    MockMvc mockMvc;

    final Faker faker = new Faker();
    final Gson gson = new Gson();

    @Test
    public void register() throws Exception {
        AuthRegister authRegister = new AuthRegister(
                faker.name().fullName(),
                faker.internet().emailAddress(),
                faker.number().digits(10),
                null,
                faker.internet().password()
        );
        String json = gson.toJson(authRegister);

        mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();
    }
}
