package com.ecom.order.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ProductResponse {

    private UUID id;

    private String name;

    private String description;

    private Double price;

    private LocalDateTime updatedAt;
}
