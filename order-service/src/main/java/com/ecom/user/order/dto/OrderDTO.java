package com.ecom.user.order.dto;

import com.ecom.user.order.entity.Order;
import com.ecom.user.order.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderDTO {

    private UUID id;

    private Double totalPrice;

    private Double totalLocalPrice;

    private List<OrderItemDTO> orderItems;

    private LocalDateTime updatedAt;

    public static OrderDTO of(Order order, List<OrderItemDTO> orderItems) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setTotalPrice(order.getTotalPrice());
        orderDTO.setTotalLocalPrice(order.getTotalLocalPrice());
        orderDTO.setOrderItems(orderItems);
        orderDTO.setUpdatedAt(order.getUpdatedAt());
        return orderDTO;
    }
}
