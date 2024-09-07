package com.yijiyap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yijiyap.modal.User;
import com.yijiyap.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void tearDown() {
        userRepository.delete(new User("test user", "test@user.com", "test@user.com"));
    }

    @Test
    public void testSignupLoginFlow() throws Exception {
        // 1. Sign up a new user
        String signupJson = "{\"fullName\":\"test user\",\"email\":\"test@user.com\",\"password\":\"test@user.com\",\"mobile\":\"test@user.com\"}";

        MvcResult signupResult = mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupJson))
                .andExpect(status().isCreated())  // 2. Assert HttpStatus.CREATED
                .andReturn();

        // 3. Attempt to sign up with the same credentials
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupJson))
                .andExpect(status().isConflict());  // 4. Assert HttpStatus.CONFLICT

        // 5. Login with the created user
        String loginJson = "{\"email\":\"test@user.com\",\"password\":\"test@user.com\"}";

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk());  // 5.1 Assert HttpStatus.OK

        // 5.2 User deletion is handled by the tearDown() method

    }
}