package com.gnt.ecom.payment.service;

import com.gnt.ecom.base.BaseService;
import com.gnt.ecom.base.PageResponse;
import com.gnt.ecom.order.event.OrderCreatedEvent;
import com.gnt.ecom.payment.dto.PaymentDTO;
import com.gnt.ecom.payment.entity.Payment;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PaymentService extends BaseService<Payment, UUID> {

    PageResponse<PaymentDTO> getAllPayments(Pageable pageable);

    PaymentDTO getById(UUID id);

    PaymentDTO getByOrder(UUID orderId);

    PaymentDTO createFromOrder(OrderCreatedEvent orderCreatedEvent);
}
