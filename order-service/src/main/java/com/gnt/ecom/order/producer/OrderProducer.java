package com.gnt.ecom.order.producer;

import com.gnt.ecom.order.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderProducer {

    @Value("${kafka.topic.order-created}")
    private String orderCreatedTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("🚀 Sending event: {}", event);
        try {
            kafkaTemplate.send(orderCreatedTopic, event.getId(), event);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
