package com.gnt.ecom.order.entity;

import com.gnt.ecom.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "orders", indexes = {@Index(name = "idx_user_id", columnList = "userId")})
public class Order extends BaseEntity {

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    private UUID userId;

    @Column(precision = 19, scale = 2)
    private BigDecimal totalPrice;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalLocalPrice;

    @Column(nullable = false, length = 3)
    private String userCurrency = "USD";

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    @Column
    private LocalDateTime paidAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod method = PaymentMethod.CASH;
}
