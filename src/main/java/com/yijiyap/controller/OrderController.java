package com.yijiyap.controller;

import com.yijiyap.modal.Order;
import com.yijiyap.modal.Wallet;
import com.yijiyap.service.OrderService;
import com.yijiyap.service.UserService;
import com.yijiyap.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private WalletService walletService;

    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(@RequestBody Order order) {

    }

}
