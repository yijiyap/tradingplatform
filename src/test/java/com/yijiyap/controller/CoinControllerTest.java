package com.yijiyap.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yijiyap.modal.Coin;
import com.yijiyap.service.CoinService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CoinControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CoinService coinService;

    @Test
    void getCoinList_ValidPage_ReturnsListOfCoins() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/coins")
                        .param("page", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void getCoinList_InvalidPage_ReturnsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/coins")
                        .param("page", "-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getCoinDetail_SavesToDB_AndReturnsDetails() throws Exception {
        String coinId = "bitcoin";

        // Perform the API call
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/coins/details/{coinId}", coinId))
                .andExpect(status().isOk())
                .andReturn();

        // Parse the response
        JsonNode responseJson = objectMapper.readTree(result.getResponse().getContentAsString());

        // Add more assertions here to ensure Coin info is correct.

        // The database will automatically clear after the test.
    }
}