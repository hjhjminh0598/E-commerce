package com.ecom.user.order.entity;

import com.ecom.user.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "orders", indexes = {@Index(name = "idx_user_id", columnList = "userId")})
public class Order extends BaseEntity {

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    private UUID userId;

    @Column(nullable = false)
    private Double totalPrice;

    private Double totalLocalPrice;

    private String userCurrency;
}
