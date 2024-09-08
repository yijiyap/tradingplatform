package com.yijiyap.service;

import com.yijiyap.domain.OrderStatus;
import com.yijiyap.modal.Order;
import com.yijiyap.modal.User;
import com.yijiyap.repository.OrderRepository;
import com.yijiyap.repository.UserRepository;
import com.yijiyap.repository.WalletRepository;
import com.yijiyap.request.CreateOrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CoinService coinService;

    @Autowired
    WalletRepository walletRepository;

    @Override
    @Transactional
    public Order createOrder(CreateOrderRequest request) {

//        Make sure not null
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Order order = new Order();
        order.setUser(user);
        order.setCoinId(request.getCoinId());
        order.setQuantity(request.getQuantity());
        order.setOrderType(request.getOrderType());
        order.setEntryPrice(request.getEntryPrice());
        order.setTakeProfitPrice(request.getTakeProfitPrice());
        order.setStopProfitPrice(request.getStopLossPrice());
        order.setTimestamp(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        return orderRepository.save(order);
    }

    @Transactional
    @Override
    public Order fillOrder(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found!"));
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Only pending orders can be filled!");
        }
        order.setStatus(OrderStatus.FILLED);

//        Calculate PnL and update wallet

        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Long id) throws Exception {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found!"));
    }

    @Override
    @Transactional
    public Order closeOrder(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found!"));

        if (order.getStatus() != OrderStatus.FILLED && order.getStatus() != OrderStatus.PARTIALLY_FILLED) {
            throw new RuntimeException("Only filled or partially filled orders can be closed!");
        }
        order.setStatus(OrderStatus.CLOSED);

//        Calculate PnL and update wallet

        return orderRepository.save(order);
    }

}
