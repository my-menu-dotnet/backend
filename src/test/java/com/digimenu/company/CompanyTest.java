package com.digimenu.company;

import com.digimenu._utils.CompanyTestHelper;
import com.digimenu._utils.UserTestHelper;
import com.digimenu.dto.CompanyRegister;
import com.digimenu.models.Company;
import com.digimenu.models.User;
import com.digimenu.security.JwtHelper;
import com.github.javafaker.Faker;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyTest {

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
    public void createCompanyWithTemToken() throws Exception {
        String tempToken = jwtHelper.generateTemporaryToken(user);

        CompanyRegister companyRegister = new CompanyRegister(
                faker.name().fullName(),
                faker.number().digits(14),
                faker.internet().emailAddress(),
                faker.phoneNumber().cellPhone()
        );
        String json = gson.toJson(companyRegister);

        mockMvc.perform(post("/company/register")
                        .header("Authorization", "Bearer " + tempToken)
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated())
//                .andExpect(header().exists("Authorization"))
                .andReturn();
    }

    @Test
    public void validateListHasOnlyAllowed() throws Exception {
        String tempToken = jwtHelper.generateTemporaryToken(user);

        MvcResult result = mockMvc.perform(post("/company")
                        .header("Authorization", "Bearer " + tempToken))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Company[] companies = gson.fromJson(content, Company[].class);

        assert companies.length == 1;

        boolean hasOnlyAllowedCompanies = Arrays.stream(companies).allMatch(
                company -> company
                        .getUsers()
                        .stream()
                        .anyMatch(user -> user.getId().equals(this.user.getId())));

        assert hasOnlyAllowedCompanies;
    }
}
