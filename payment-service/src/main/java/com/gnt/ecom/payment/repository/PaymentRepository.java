package com.gnt.ecom.payment.repository;

import com.gnt.ecom.base.BaseRepository;
import com.gnt.ecom.payment.entity.Payment;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends BaseRepository<Payment, UUID> {

    Optional<Payment> findByOrderId(UUID orderId);
}
