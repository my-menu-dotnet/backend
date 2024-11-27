package net.mymenu.auth;

import net.mymenu._utils.CompanyTestHelper;
import net.mymenu._utils.UserTestHelper;
import net.mymenu.dto.auth.AuthLogin;
import net.mymenu.dto.auth.AuthLoginCompany;
import net.mymenu.models.Company;
import net.mymenu.models.User;
import net.mymenu.security.JwtHelper;
import com.github.javafaker.Faker;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public final class LoginTest {

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public JwtHelper jwtHelper;

    @Autowired
    public UserTestHelper userTestHelper;

    @Autowired
    public CompanyTestHelper companyTestHelper;

    final public Faker faker = new Faker();
    final public Gson gson = new Gson();
    public User user;
    public Company company;

    @BeforeEach
    public void setup() {
        user = userTestHelper.createUserIfNotExists();
        company = companyTestHelper.createCompanyIfNotExists(user);
    }

    @Test
    public void login() throws Exception {
        AuthLogin authLogin = new AuthLogin(user.getEmail(), user.getPassword());
        String json = gson.toJson(authLogin);

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andReturn();
    }

    @Test
    public void loginWithCompany() throws Exception {
        String tempToken = jwtHelper.generateUserToken(user);

        AuthLoginCompany authLoginCompany = new AuthLoginCompany();
        authLoginCompany.setCompanyId(company.getId());
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
        AuthLogin authLogin = new AuthLogin(user.getEmail(), "wrongPassword");
        String json = gson.toJson(authLogin);

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isForbidden());
    }
}
