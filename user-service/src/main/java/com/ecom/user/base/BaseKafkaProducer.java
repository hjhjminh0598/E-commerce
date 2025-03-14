package com.ecom.user.base;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseKafkaProducer {

    protected final String topic;

    protected final KafkaTemplate<String, Object> kafkaTemplate;

    public BaseKafkaProducer(String topic, KafkaTemplate<String, Object> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(String key, Object event) {
        log.info("🚀 Sending event: {}", event);
        kafkaTemplate.send(topic, key, event)
                .thenApply(result -> logSuccess(result, key, event))
                .exceptionally(ex -> {
                    logFailure(ex, key, event);
                    return null;
                });
    }

    private Void logSuccess(SendResult<String, Object> result, String key, Object message) {
        RecordMetadata metadata = result.getRecordMetadata();
        log.info("✅ Message sent: Topic: {} | Partition: {} | Offset: {} | Key: {} | Message: {}",
                metadata.topic(), metadata.partition(), metadata.offset(), key, message);
        return null;
    }

    private void logFailure(Throwable ex, String key, Object message) {
        log.error("❌ Message failed: Key = {}, Message = {}", key, message, ex);
    }
}