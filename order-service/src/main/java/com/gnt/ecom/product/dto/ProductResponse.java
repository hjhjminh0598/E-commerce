package com.gnt.ecom.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ProductResponse {

    private UUID id;

    private String name;

    private String description;

    private BigDecimal price;

    private LocalDateTime updatedAt;
}
