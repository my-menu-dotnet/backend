package com.digimenu.auth;

import com.digimenu.dto.AuthLogin;
import com.digimenu.dto.AuthLoginCompany;
import com.digimenu.dto.CompanyRegister;
import com.digimenu.models.Company;
import com.digimenu.repository.CompanyRepository;
import com.digimenu.security.JwtHelper;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public final class LoginTest extends Auth {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JwtHelper jwtHelper;

    @Autowired
    CompanyRepository companyRepository;

    @Test
    public void login() throws Exception {
        AuthLogin authLogin = new AuthLogin(email, password);
        String json = gson.toJson(authLogin);

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andReturn();
    }

    @Test
    public void createCompanyWithTemToken() throws Exception {
        String tempToken = jwtHelper.generateTemporaryToken(Auth.user);

        CompanyRegister companyRegister = new CompanyRegister(
                faker.name().fullName(),
                faker.number().digits(14),
                faker.internet().emailAddress(),
                faker.phoneNumber().cellPhone()
        );
        String json = gson.toJson(companyRegister);

        MvcResult result = mockMvc.perform(post("/company/register")
                        .header("Authorization", "Bearer " + tempToken)
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated())
//                .andExpect(header().exists("Authorization"))
                .andReturn();

        Auth.company = gson.fromJson(result.getResponse().getContentAsString(), Company.class);
    }

    @Test
    public void loginWithCompany() throws Exception {
        String tempToken = jwtHelper.generateTemporaryToken(Auth.user);

        AuthLoginCompany authLoginCompany = new AuthLoginCompany();
        authLoginCompany.setCompanyId(Auth.company.getId());
        String json = gson.toJson(authLoginCompany);

        mockMvc.perform(post("/auth/login/company")
                        .header("Authorization", "Bearer " + tempToken)
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"));
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
