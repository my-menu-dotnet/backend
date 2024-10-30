package com.digimenu.auth;

import com.digimenu.dto.AuthRegister;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@Order(1)
public class RegisterTest extends Auth {

    @Test
    public void register() throws Exception {
        AuthRegister authRegister = new AuthRegister(name, email, cpf, null, password);
        String json = gson.toJson(authRegister);

        mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated());
    }
}
