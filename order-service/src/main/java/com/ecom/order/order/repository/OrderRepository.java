package com.ecom.order.order.repository;

import com.ecom.order.base.BaseRepository;
import com.ecom.order.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends BaseRepository<Order, UUID> {

    @Query(value = "SELECT o FROM Order o WHERE o.userId = :userId AND o.deletedAt IS NULL",
            countQuery = "SELECT COUNT(o) FROM Order o WHERE o.userId = :userId AND o.deletedAt IS NULL")
    Page<Order> findAllByUserId(UUID userId, Pageable pageable);
}
