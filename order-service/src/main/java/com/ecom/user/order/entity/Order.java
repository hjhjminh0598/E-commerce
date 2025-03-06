package com.ecom.user.order.entity;

import com.ecom.user.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "orders", indexes = {@Index(name = "idx_user_id", columnList = "userId")})
public class Order extends BaseEntity {

    @Column(nullable = false)
    private UUID userId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

    @Column(nullable = false)
    private Double totalPrice;

    private Double totalLocalPrice;

    private String userCurrency;
}
