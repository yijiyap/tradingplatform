package com.yijiyap.request;

import com.yijiyap.domain.OrderType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CreateOrderRequest {
    private Long userId;
    private String coinId;
    private double quantity;
    private OrderType orderType;
    private BigDecimal entryPrice;
    private BigDecimal takeProfitPrice;
    private BigDecimal stopLossPrice;
}