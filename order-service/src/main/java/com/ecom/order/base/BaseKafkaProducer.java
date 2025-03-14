package com.ecom.order.base;

public interface BaseKafkaProducer<K, V> {

    void send(String topic, K key, V message);
}
