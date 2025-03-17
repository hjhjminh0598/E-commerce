package com.gnt.ecom.payment.entity;

import com.gnt.ecom.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "payments", indexes = {@Index(name = "idx_order_id", columnList = "orderId")})
public class Payment extends BaseEntity {

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    private UUID orderId;

    @Column(nullable = false)
    private Double totalPrice;

    private Double totalLocalPrice;

    private String userCurrency;

    private PaymentStatus status = PaymentStatus.WAITING;

    private PaymentMethod method = PaymentMethod.CASH;
}
