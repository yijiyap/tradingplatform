package com.yijiyap.service;

import com.yijiyap.domain.OrderStatus;
import com.yijiyap.domain.OrderType;
import com.yijiyap.modal.Coin;
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
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CoinService coinService;

    @Override
    @Transactional
    public Order createOrder(CreateOrderRequest request) {

//        Make sure not null
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found!"));

//        Make sure not null
        Coin coin = coinService.getCoinById(request.getCoinId());

        Order order = new Order();
        order.setUser(user);
        order.setCoin(coin);
        order.setQuantity(request.getQuantity());
        order.setOrderType(request.getOrderType());
        order.setEntryPrice(request.getEntryPrice());
        order.setTakeProfitPrice(request.getTakeProfitPrice());
        order.setStopProfitPrice(request.getStopLossPrice());
        order.setTimestamp(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Long id) throws Exception {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found!"));
    }

}
