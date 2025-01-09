package net.mymenu.auth;

import net.mymenu.dto.auth.AuthRegister;
import com.github.javafaker.Faker;
import com.google.gson.Gson;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RegisterTest {

//    @Autowired
//    MockMvc mockMvc;
//
//    final Faker faker = new Faker();
//    final Gson gson = new Gson();
//
//    @Test
//    @Disabled
//    public void register() throws Exception {
//        AuthRegister authRegister = new AuthRegister(
//                faker.name().fullName(),
//                faker.internet().emailAddress(),
//                faker.number().digits(10),
//                null,
//                faker.internet().password()
//        );
//        String json = gson.toJson(authRegister);
//
//        mockMvc.perform(post("/auth/register")
//                        .contentType("application/json")
//                        .content(json))
//                .andExpect(status().isCreated())
//                .andReturn();
//    }
}
