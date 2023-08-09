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
    @SpringBootTest
    @AutoConfigureMockMvc
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public class TestControllerUser {
        @Autowired
        MockMvc mockMvc;

        @Test
        @WithMockUser(username = "user",password = "user")
        void getAllUsersTest() throws Exception {
            JSONObject jsonObject = new JSONObject();
            mockMvc.perform(get("/user/list")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonObject.toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        }

        @Test
        @WithMockUser(authorities = {"admin:write"})
        void getAllUsersTestNegative() throws Exception {
            mockMvc.perform(get("/user/list")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(username = "admin",password = "admin")
        void createUserTest() throws Exception {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", "test_name");
            jsonObject.put("password", "7852");

            mockMvc.perform(post("/user")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonObject.toString()))
                    .andExpect(status().isOk());
        }

        @Test
        void createUserTestWitchToken() throws Exception {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", "test_name");
            jsonObject.put("password", "7852");
            mockMvc.perform(post("/user")
                            .header("Authorization", "Bearer admin")
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
        @WithMockUser("test_name")
        void getMyProfileTest() throws Exception {
            JSONObject jsonObject = new JSONObject();
            mockMvc.perform(get("/user/me")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonObject.toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("test_name"));
        }
    }
