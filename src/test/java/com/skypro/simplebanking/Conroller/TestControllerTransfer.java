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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)

public class TestControllerTransfer {
    @Value("${app.security.admin-token}")
    private String adminToken;

    @Autowired
    MockMvc mockMvc;

    @Test
    void transferTest() throws Exception {
        JSONObject testUser = new JSONObject();
        testUser.put("username", "test_name");
        testUser.put("password", "$2a$12$xYtql9oLgbaq/FUaYu2uieQqSIp1brc0HKtXvqI2WPQUR77pyTE2q");
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testUser.toString())
                        .header("X-SECURITY-ADMIN-KEY", adminToken))
                .andExpect(status().isOk());
        JSONObject testUser2 = new JSONObject();
        testUser.put("username", "test_name2");
        testUser.put("password", "$2a$12$xYtql9oLgbaq/FUaYu2uieQqSIp1brc0HKtXvqI2WPQUR77pyTE2q");
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testUser.toString())
                        .header("X-SECURITY-ADMIN-KEY", adminToken))
                .andExpect(status().isOk());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fromAccountId", 1);
        jsonObject.put("toUserId", 2);
        jsonObject.put("toAccountId", 4);
        jsonObject.put("amount", 1);
        BankingUserDetails bankingUserDetails = new BankingUserDetails(1L, "test_name", "7852", false);
        mockMvc.perform(post("/transfer").with(user(bankingUserDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk());
    }

    @Test
    void transferTestNegative() throws Exception {
        mockMvc.perform(post("/transfer")
                        .header("X-SECURITY-ADMIN-KEY", adminToken))
                .andExpect(status().isForbidden());
    }
}
