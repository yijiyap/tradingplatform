package com.yijiyap.modal;

import com.yijiyap.domain.OrderStatus;
import com.yijiyap.domain.OrderType;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //    User who made the order
    @ManyToOne
    private User user;

    private double quantity;

    private String coinId;

    @Column(nullable = false)
    private OrderType orderType;

    @Column(nullable = false)
    private BigDecimal entryPrice;

    private BigDecimal takeProfitPrice;

    private BigDecimal stopProfitPrice;

    private LocalDateTime timestamp = LocalDateTime.now();

    @Column(nullable = false)
    private OrderStatus status;

}
