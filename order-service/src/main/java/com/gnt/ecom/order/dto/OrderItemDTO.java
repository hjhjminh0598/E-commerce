package com.gnt.ecom.order.dto;

import com.gnt.ecom.order.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class OrderItemDTO {

    private UUID id;

    private UUID productId;

    private String productName;

    private String description;

    private Integer quantity;

    private BigDecimal price;

    private LocalDateTime updatedAt;

    public static OrderItemDTO of(OrderItem orderItem) {
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setId(orderItem.getId());
        orderItemDTO.setProductId(orderItem.getProductId());
        orderItemDTO.setQuantity(orderItem.getQuantity());
        orderItemDTO.setPrice(orderItem.getPrice());
        orderItemDTO.setUpdatedAt(orderItem.getUpdatedAt());
        return orderItemDTO;
    }
}
