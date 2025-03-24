package com.gnt.ecom.order.consumer;

import com.gnt.ecom.order.event.OrderCreatedEvent;
import com.gnt.ecom.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderConsumer {

    private final PaymentService paymentService;

    @KafkaListener(topics = "${kafka.topic.order-created}", groupId = "${kafka.consumer.payment-group}", containerFactory = "orderKafkaListenerContainerFactory")
    public void consumeOrderCreated(ConsumerRecord<String, OrderCreatedEvent> record, @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
        try {
            log.info("Received Order: partition {} {}", record.partition(), record.value());
            paymentService.createFromOrder(record.value());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}