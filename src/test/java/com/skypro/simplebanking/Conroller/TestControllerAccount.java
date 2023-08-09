package com.skypro.simplebanking.Conroller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TestControllerAccount {
    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(username = "test_name", password = "7852")
    void getAccountTest() throws Exception {
        mockMvc.perform(get("/account/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user",password = "7852")
    void getAccountTestNegative() throws Exception {
        mockMvc.perform(get("/account/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "test_name",password = "7852")
    void depositTest() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amount", 5);
        mockMvc.perform(post("/account/deposit/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user",password = "user")
    void depositTestNegative() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amount", 5);
        mockMvc.perform(post("/account/deposit/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "test_name",password = "7852")
    void withdrawTest() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amount", 1);
        mockMvc.perform(post("/account/withdraw/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user",password = "user")
    void withdrawTestNegative() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amount", 1);
        mockMvc.perform(post("/account/withdraw/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isForbidden());
    }
}
