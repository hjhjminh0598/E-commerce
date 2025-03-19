package com.gnt.ecom.payment.dto;

import com.gnt.ecom.payment.entity.Payment;
import com.gnt.ecom.payment.entity.PaymentMethod;
import com.gnt.ecom.payment.entity.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class PaymentDTO {

    private UUID id;

    private BigDecimal totalPrice;

    private BigDecimal totalLocalPrice;

    private String userCurrency;

    private PaymentStatus status = PaymentStatus.WAITING;

    private PaymentMethod method = PaymentMethod.CASH;

    private String transactionId;

    public static PaymentDTO of(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment.getId());
        dto.setTotalPrice(payment.getTotalPrice());
        dto.setTotalLocalPrice(payment.getTotalLocalPrice());
        dto.setUserCurrency(payment.getUserCurrency());
        dto.setStatus(payment.getStatus());
        dto.setMethod(payment.getMethod());
        dto.setTransactionId(payment.getTransactionId());

        return dto;
    }
}
