package com.yijiyap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yijiyap.domain.OrderStatus;
import com.yijiyap.domain.OrderType;
import com.yijiyap.modal.Coin;
import com.yijiyap.modal.Order;
import com.yijiyap.modal.User;
import com.yijiyap.repository.OrderRepository;
import com.yijiyap.request.CreateOrderRequest;
import com.yijiyap.response.OrderResponse;
import com.yijiyap.service.OrderService;
import com.yijiyap.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderController orderController;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private User testUser;
    private Order testOrder;
    private CreateOrderRequest testCreateOrderRequest;

    @BeforeEach
    void setUp() throws Exception {
        testUser = new User();
        testUser.setId(1L);

        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setUser(testUser);
        testOrder.setCoinId("BTC");
        testOrder.setQuantity(1.0);
        testOrder.setOrderType(OrderType.BUY_MARKET);
        testOrder.setEntryPrice(new BigDecimal("50000"));
        testOrder.setStatus(OrderStatus.FILLED);
        testOrder.setTimestamp(LocalDateTime.now());

        testCreateOrderRequest = new CreateOrderRequest();
        testCreateOrderRequest.setCoinId("BTC");
        testCreateOrderRequest.setQuantity(1.0);
        testCreateOrderRequest.setOrderType(OrderType.BUY_MARKET);
        testCreateOrderRequest.setEntryPrice(new BigDecimal("50000"));

        when(userService.findUserProfileByJwt(anyString())).thenReturn(testUser);
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void placeOrder_Success() throws Exception {
        String jwt = "test-jwt";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/order/place")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCreateOrderRequest)))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        OrderResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), OrderResponse.class);
        assertNotNull(response);

        // Verify order in database
        Optional<Order> savedOrder = orderRepository.findById(response.getId());
        assertTrue(savedOrder.isPresent());
        assertEquals(testUser.getId(), savedOrder.get().getUser().getId());
    }

    @Test
    void closeOrder_Success() throws Exception {
        String jwt = "test-jwt";

//        Save test order in repo
        Order savedOrder = orderRepository.save(testOrder);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/order/{orderId}/close}", savedOrder.getId())
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCreateOrderRequest)))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        OrderResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), OrderResponse.class);
        assertNotNull(response);

        // Verify order in database
        Optional<Order> closedOrder = orderRepository.findById(response.getId());
        assertTrue(closedOrder.isPresent());
        assertEquals(testUser.getId(), closedOrder.get().getUser().getId());
        assertEquals(OrderStatus.CLOSED, closedOrder.get().getStatus());  // Assuming you have an OrderStatus enum

        // Additional assertions to verify other details of the closed order
        assertEquals(testOrder.getCoinId(), closedOrder.get().getCoinId());
        assertEquals(testOrder.getQuantity(), closedOrder.get().getQuantity());
        assertEquals(testOrder.getOrderType(), closedOrder.get().getOrderType());
        assertEquals(testOrder.getEntryPrice(), closedOrder.get().getEntryPrice());
    }
}