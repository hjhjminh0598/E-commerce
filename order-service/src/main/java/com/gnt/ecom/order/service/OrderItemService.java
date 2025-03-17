package com.gnt.ecom.order.service;

import com.gnt.ecom.base.BaseService;
import com.gnt.ecom.order.dto.OrderItemDTO;
import com.gnt.ecom.order.entity.OrderItem;
import java.util.List;
import java.util.UUID;

public interface OrderItemService extends BaseService<OrderItem, UUID> {

    List<OrderItemDTO> getByOrderId(UUID orderId);
}
