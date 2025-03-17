package com.gnt.ecom.order.service;

import com.gnt.ecom.base.BaseService;
import com.gnt.ecom.base.PageResponse;
import com.gnt.ecom.order.dto.CreateOrderRequest;
import com.gnt.ecom.order.dto.OrderDTO;
import com.gnt.ecom.order.entity.Order;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderService extends BaseService<Order, UUID> {

    PageResponse<OrderDTO> getAllOrders(Pageable pageable);

    PageResponse<OrderDTO> getByUser(UUID userId, Pageable pageable);

    OrderDTO getById(UUID id);

    OrderDTO create(CreateOrderRequest request);
}
