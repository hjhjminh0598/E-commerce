package com.gnt.ecom.payment.service;

import com.gnt.ecom.base.BaseRepository;
import com.gnt.ecom.base.BaseServiceImpl;
import com.gnt.ecom.base.PageResponse;
import com.gnt.ecom.order.event.OrderCreatedEvent;
import com.gnt.ecom.payment.dto.PaymentDTO;
import com.gnt.ecom.payment.entity.Payment;
import com.gnt.ecom.payment.entity.PaymentMethod;
import com.gnt.ecom.payment.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
public class PaymentServiceImpl extends BaseServiceImpl<Payment, UUID> implements PaymentService {

    private final PaymentRepository paymentRepository;

    protected PaymentServiceImpl(BaseRepository<Payment, UUID> repository, PaymentRepository paymentRepository) {
        super(repository);
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PageResponse<PaymentDTO> getAllPayments(Pageable pageable) {
        return PageResponse.from(super.findAll(pageable).map(PaymentDTO::of));
    }

    @Override
    public PaymentDTO getById(UUID id) {
        return super.findById(id).map(PaymentDTO::of).orElse(null);
    }

    @Override
    public PaymentDTO getByOrder(UUID orderId) {
        return paymentRepository.findByOrderId(orderId).map(PaymentDTO::of).orElse(null);
    }

    @Override
    public PaymentDTO createFromOrder(OrderCreatedEvent orderCreatedEvent) {
        Payment payment = new Payment();
        payment.setOrderId(UUID.fromString(orderCreatedEvent.getId()));
        payment.setTotalPrice(new BigDecimal(orderCreatedEvent.getTotalPrice()));
        payment.setTotalLocalPrice(new BigDecimal(orderCreatedEvent.getTotalLocalPrice()));
        payment.setUserCurrency(orderCreatedEvent.getUserCurrency());
        payment.setMethod(PaymentMethod.valueOf(orderCreatedEvent.getMethod()));

        return PaymentDTO.of(save(payment));
    }
}
