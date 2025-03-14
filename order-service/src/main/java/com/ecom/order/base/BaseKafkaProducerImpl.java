package com.ecom.order.base;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public abstract class BaseKafkaProducerImpl<K, V> implements BaseKafkaProducer<K, V> {

    protected final KafkaTemplate<K, V> kafkaTemplate;

    public BaseKafkaProducerImpl(KafkaTemplate<K, V> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(String topic, K key, V message) {
        kafkaTemplate.send(topic, key, message)
                .thenApply(result -> logSuccess(result, key, message))
                .exceptionally(ex -> {
                    logFailure(ex, key, message);
                    return null;
                });
    }

    private Void logSuccess(SendResult<K, V> result, K key, V message) {
        RecordMetadata metadata = result.getRecordMetadata();
        log.info("✅ Message sent to Kafka: Topic: {} | Partition: {} | Offset: {} | Key: {} | Message: {}", metadata.topic(), metadata.partition(), metadata.offset(), key, message);
        return null;
    }

    private void logFailure(Throwable ex, K key, V message) {
        log.error("❌ Kafka message failed: Key = {}, Message = {}", key, message, ex);
    }
}
