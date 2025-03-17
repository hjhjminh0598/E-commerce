package com.gnt.ecom.order.consumer;

import com.gnt.ecom.order.event.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderConsumer {

    @KafkaListener(topics = "${kafka.topic.order-created}", groupId = "${kafka.consumer.payment-group}", containerFactory = "orderKafkaListenerContainerFactory")
    public void consumeOrderCreated(OrderCreatedEvent order) {
        log.info("Received Order: {}", order);
    }
}
