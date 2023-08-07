package com.skypro.simplebanking.Conroller;

import static org.assertj.core.api.BDDAssumptions.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.skypro.simplebanking.controller.UserController;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.JsonPath;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TestControllers {
    @Autowired
    MockMvc mockMvc;
    @Test
    @WithMockUser("user")
    void getAllUsersTest() throws Exception{
        JSONObject jsonObject = new JSONObject() ;
        mockMvc.perform(get("/user/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
    @Test
    @WithMockUser("admin")
    void getAllUsersTestNegative() throws Exception{
        mockMvc.perform(get("/user/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isForBidden());
    }
    @Test
    @WithMockUser(authorities = {"user:write"})
    void createUserTest() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username","test_name");
        jsonObject.put("password","7852");

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject.toString()))
                .andExpect(status().isOk());
    }
    @Test
    vois createUserTestWitchTocken() throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username","test_name");
        jsonObject.put("password","7852");
        mockMvc.perform(post("/user")
            .header("Authorization", "Bearer admin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonObject.toString())) 
            .andExpect(status().isOk());
    } 
    @Test
    @WithMockUser("user") 
    void createUserTestNegative() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username","test_name");
        jsonObject.put("password","7852");

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject.toString()))
                .andExpect(status().isForBidden());
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
    @Test
    @WithMockUser("test_name")
    void transferTest()throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fromAccountId",1);
        jsonObject.put("toUserId",3);
        jsonObject.put("toAccountId",55);
        jsonObject.put("amount",1);
        mockMvc.perform(post("/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject.toString()))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser("user")
    void transferTestNegative()throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fromAccountId",1);
        jsonObject.put("toUserId",3);
        jsonObject.put("toAccountId",55);
        jsonObject.put("amount",1);
        mockMvc.perform(post("/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject.toString()))
                .andExpect(status().isForBidden());
    }
    @Test
    @WithMockUser("test_name")
    void getAccountTest() throws Exception{
        mockMvc.prform(get("/account/1"))
            .andExpect(status().isOk()) 
            } 
    @Test
    @WithMockUser("user")
    void getAccountTest() throws Exception{
        mockMvc.prform(get("/account/1"))
            .andExpect(status().isOk()) 
            } 
                     
    @Test
    @WithMockUser(authorities = {"test_name"})
    void depositTest() throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amount",5);
        mockMvc.perform(post("/account/deposit/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject.toString()))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(authorities = {"user"})
    void depositTestNegative() throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amount",5);
        mockMvc.perform(post("/account/deposit/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject.toString()))
                .andExpect(status().isForBidden());
    }
    @Test
    @WithMockUser(authorities = {"test_name"})
    void withdrawTest() throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amount",1);
        mockMvc.perform(post("/account/withdraw/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(authorities = {"user"})
    void withdrawTestNegative() throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amount",1);
        mockMvc.perform(post("/account/withdraw/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isForBidden());
    }
    
}
