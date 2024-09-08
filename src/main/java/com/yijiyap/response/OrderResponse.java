package com.yijiyap.response;

import com.yijiyap.domain.OrderStatus;
import com.yijiyap.domain.OrderType;
import com.yijiyap.modal.Order;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderResponse {
    private Long id;
    private Long userId;
    private String coinId;
    private double quantity;
    private OrderType orderType;
    private BigDecimal entryPrice;
    private OrderStatus status;
    private LocalDateTime timestamp;

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.userId = order.getUser().getId();
        this.coinId = order.getCoinId();
        this.quantity = order.getQuantity();
        this.orderType = order.getOrderType();
        this.entryPrice = order.getEntryPrice();
        this.status = order.getStatus();
        this.timestamp = order.getTimestamp();
    }

}
