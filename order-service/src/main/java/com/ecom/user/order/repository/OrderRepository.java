package com.ecom.user.order.repository;

import com.ecom.user.base.BaseRepository;
import com.ecom.user.order.entity.Order;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends BaseRepository<Order, UUID> {


}
