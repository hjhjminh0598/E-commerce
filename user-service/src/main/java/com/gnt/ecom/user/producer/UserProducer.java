package com.gnt.ecom.user.producer;

import com.gnt.ecom.base.BaseKafkaProducer;
import com.gnt.ecom.user.event.UserCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserProducer extends BaseKafkaProducer {

    public UserProducer(@Value("${kafka.topic.user-created}") String topic, KafkaTemplate<String, Object> kafkaTemplate) {
        super(topic, kafkaTemplate);
    }

    public void publishUserCreatedEvent(UserCreatedEvent user) {
        log.info("🚀 Sending UserCreatedEvent: {}", user);
        super.send(user.getId(), user.toString());
    }
}
