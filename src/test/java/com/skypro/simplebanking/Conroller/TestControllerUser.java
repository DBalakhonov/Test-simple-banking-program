package com.skypro.simplebanking.Conroller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
    @SpringBootTest
    @AutoConfigureMockMvc
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public class TestControllerUser {
        @Autowired
        MockMvc mockMvc;

        @Test
        @WithMockUser(username = "user",password = "user")
        void getAllUsersTest() throws Exception {
            mockMvc.perform(get("/user/list"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        }

        @Test
        void getAllUsersTestNegative() throws Exception {
            mockMvc.perform(get("/user/list")
                            .header("X-SECURITY-ADMIN-KEY","admin"))
                    .andExpect(status().isForbidden());
        }
        @Test
        void createUserTestWitchToken() throws Exception {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", "test_name");
            jsonObject.put("password", "7852");
            mockMvc.perform(post("/user")
                            .header("X-SECURITY-ADMIN-KEY", "admin")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonObject.toString()))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "user", password = "user")
        void createUserTestNegative() throws Exception {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", "test_name");
            jsonObject.put("password", "7852");

            mockMvc.perform(post("/user")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonObject.toString()))
                    .andExpect(status().isForbidden());
        }

        @Test
        void getMyProfileTest() throws Exception {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", "test_name");
            jsonObject.put("password", "$2a$12$xYtql9oLgbaq/FUaYu2uieQqSIp1brc0HKtXvqI2WPQUR77pyTE2q");
            mockMvc.perform(post("/user")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonObject.toString())
                            .header("X-SECURITY-ADMIN-KEY", "admin"))
                    .andExpect(status().isOk());
            mockMvc.perform(get("/user/me").with(user("test_name").password("7852")))
                    .andExpect(status().isOk());
        }
    }
