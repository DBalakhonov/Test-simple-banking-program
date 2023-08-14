package com.skypro.simplebanking.Conroller;

import com.skypro.simplebanking.dto.BankingUserDetails;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TestControllerAccount {
    @Value("${app.security.admin-token}")
    private String adminToken;

    @Autowired
    MockMvc mockMvc;

    @Test
    void getAccountTest() throws Exception {
        JSONObject testUser = new JSONObject();
        testUser.put("username", "test_name");
        testUser.put("password", "$2a$12$xYtql9oLgbaq/FUaYu2uieQqSIp1brc0HKtXvqI2WPQUR77pyTE2q");
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testUser.toString())
                        .header("X-SECURITY-ADMIN-KEY", adminToken))
                .andExpect(status().isOk());
        BankingUserDetails bankingUserDetails = new BankingUserDetails(1L, "test_name", "7852", false);
        mockMvc.perform(get("/account/1").with(user(bankingUserDetails))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }
    @Test
    void getAccountTestNegative() throws Exception {
        mockMvc.perform(get("/account/1")
                        .header("X-SECURITY-ADMIN-KEY", adminToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void depositTest() throws Exception {
        JSONObject testUser = new JSONObject();
        testUser.put("username", "test_name");
        testUser.put("password", "$2a$12$xYtql9oLgbaq/FUaYu2uieQqSIp1brc0HKtXvqI2WPQUR77pyTE2q");
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testUser.toString())
                        .header("X-SECURITY-ADMIN-KEY", adminToken))
                .andExpect(status().isOk());
        BankingUserDetails bankingUserDetails = new BankingUserDetails(1L, "test_name", "7852", false);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amount", 5);
        mockMvc.perform(post("/account/deposit/1").with(user(bankingUserDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(1+jsonObject.getInt("amount")));
    }

    @Test
    void depositTestNegative() throws Exception {
        mockMvc.perform(post("/account/deposit/1")
                        .header("X-SECURITY-ADMIN-KEY", adminToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void withdrawTest() throws Exception {
        JSONObject testUser = new JSONObject();
        testUser.put("username", "test_name");
        testUser.put("password", "$2a$12$xYtql9oLgbaq/FUaYu2uieQqSIp1brc0HKtXvqI2WPQUR77pyTE2q");
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testUser.toString())
                        .header("X-SECURITY-ADMIN-KEY", adminToken))
                .andExpect(status().isOk());
        BankingUserDetails bankingUserDetails = new BankingUserDetails(1L, "test_name", "7852", false);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amount", 1);
        mockMvc.perform(post("/account/withdraw/1").with(user(bankingUserDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(0));
    }

    @Test
    void withdrawTestNegative() throws Exception {
        mockMvc.perform(post("/account/withdraw/1")
                        .header("X-SECURITY-ADMIN-KEY", adminToken))
                .andExpect(status().isForbidden());
    }
}
