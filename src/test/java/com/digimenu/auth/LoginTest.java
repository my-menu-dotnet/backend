package com.digimenu.auth;

import com.digimenu.dto.AuthLogin;
import com.digimenu.dto.AuthLoginCompany;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class LoginTest extends Auth {

    @Test
    public void login() throws Exception {
        AuthLogin authLogin = new AuthLogin(email, password);
        String json = gson.toJson(authLogin);

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"));
    }

    @Test
    public void loginWithCompany() throws Exception {
        AuthLoginCompany authLoginCompany = new AuthLoginCompany();
    }

    @Test
    public void loginFail() throws Exception {
        AuthLogin authLogin = new AuthLogin(email, "wrongPassword");
        String json = gson.toJson(authLogin);

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isForbidden());
    }
}
