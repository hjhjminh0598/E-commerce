package com.gnt.ecom.user.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserCreatedEventConsumer {

    @KafkaListener(topics = "${kafka.topic.user-created}", groupId = "${kafka.consumer.product-group}")
    public void consumeMessage(String event) {
        log.info("\uD83D\uDCE5 Received UserCreatedEvent: {}", event);
    }
}
