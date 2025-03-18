package com.gnt.ecom.payment.entity;

import com.gnt.ecom.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "payments", indexes = {@Index(name = "idx_order_id", columnList = "orderId")})
public class Payment extends BaseEntity {

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    private UUID orderId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalPrice;

    @Column(precision = 19, scale = 2)
    private BigDecimal totalLocalPrice;

    @Column(nullable = false, length = 3)
    private String userCurrency = "USD";

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.WAITING;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod method = PaymentMethod.CASH;

    @Column(length = 100)
    private String transactionId;
}
