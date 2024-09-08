package com.yijiyap.controller;

import com.yijiyap.modal.Order;
import com.yijiyap.modal.User;
import com.yijiyap.modal.Wallet;
import com.yijiyap.request.CreateOrderRequest;
import com.yijiyap.service.OrderService;
import com.yijiyap.service.UserService;
import com.yijiyap.service.WalletService;
import com.yijiyap.response.OrderResponse;
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
    public ResponseEntity<OrderResponse> placeOrder(
            @RequestHeader("Authorization") String jwt,
            @RequestBody CreateOrderRequest request) {

        try {
            User user = userService.findUserProfileByJwt(jwt);
            request.setUserId(user.getId());
            Order createdOrder = orderService.createOrder(request);

            return ResponseEntity.ok(new OrderResponse(createdOrder));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @PostMapping("/{orderId}/fill")
    public ResponseEntity<OrderResponse> fillOrder(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long orderId) {

        try {
            User user = userService.findUserProfileByJwt(jwt);
            Order filledOrder = orderService.fillOrder(user.getId(), orderId);
            return ResponseEntity.ok(new OrderResponse(filledOrder));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/{orderId}/close")
    public ResponseEntity<OrderResponse> closeOrder(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long orderId) {

        try {
            User user = userService.findUserProfileByJwt(jwt);
            Order closedOrder = orderService.closeOrder(user.getId(), orderId);
            return ResponseEntity.ok(new OrderResponse(closedOrder));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
