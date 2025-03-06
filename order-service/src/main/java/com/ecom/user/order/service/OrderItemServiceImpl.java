package com.ecom.user.order.service;

import com.ecom.user.base.BaseRepository;
import com.ecom.user.base.BaseServiceImpl;
import com.ecom.user.order.dto.OrderItemDTO;
import com.ecom.user.order.entity.OrderItem;
import com.ecom.user.order.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderItemServiceImpl extends BaseServiceImpl<OrderItem, UUID> implements OrderItemService {

    private final OrderItemRepository orderItemRepository;

    protected OrderItemServiceImpl(BaseRepository<OrderItem, UUID> repository, OrderItemRepository orderItemRepository) {
        super(repository);
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public List<OrderItemDTO> getItemsByOrderId(UUID orderId) {
        return orderItemRepository.findAllByOrderId(orderId).stream()
                .map(OrderItemDTO::of).toList();
    }
}
