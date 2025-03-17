package com.gnt.ecom.order.repository;

import com.gnt.ecom.base.BaseRepository;
import com.gnt.ecom.order.entity.OrderItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderItemRepository extends BaseRepository<OrderItem, UUID> {

    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id = :orderId AND oi.deletedAt IS NULL")
    List<OrderItem> findByOrderId(UUID orderId);
}
