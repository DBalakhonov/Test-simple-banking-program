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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)

public class TestControllerTransfer {

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(username = "test_name",password = "7852")
    void transferTest() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fromAccountId", 1);
        jsonObject.put("toUserId", 3);
        jsonObject.put("toAccountId", 55);
        jsonObject.put("amount", 1);
        mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user",password = "user")
    void transferTestNegative() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fromAccountId", 1);
        jsonObject.put("toUserId", 3);
        jsonObject.put("toAccountId", 55);
        jsonObject.put("amount", 1);
        mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isForbidden());
    }
}
