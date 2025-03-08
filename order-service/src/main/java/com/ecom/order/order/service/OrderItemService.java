package com.ecom.order.order.service;

import com.ecom.order.base.BaseService;
import com.ecom.order.order.dto.OrderItemDTO;
import com.ecom.order.order.entity.OrderItem;
import java.util.List;
import java.util.UUID;

public interface OrderItemService extends BaseService<OrderItem, UUID> {

    List<OrderItemDTO> getByOrderId(UUID orderId);
}
