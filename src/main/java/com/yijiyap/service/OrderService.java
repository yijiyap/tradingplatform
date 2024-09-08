package com.yijiyap.service;

import com.yijiyap.domain.OrderType;
import com.yijiyap.modal.Order;
import com.yijiyap.modal.User;
import com.yijiyap.request.CreateOrderRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderService {
    Order createOrder(CreateOrderRequest request);

    Order closeOrder(Long userId, Long orderId);

    Order getOrderById(Long id) throws Exception;

}
