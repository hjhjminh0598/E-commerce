package com.gnt.ecom.order.consumer;

import com.gnt.ecom.order.event.OrderCreatedEvent;
import com.gnt.ecom.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderConsumer {

    private final PaymentService paymentService;

    @KafkaListener(topics = "${kafka.topic.order-created}", groupId = "${kafka.consumer.payment-group}", containerFactory = "orderKafkaListenerContainerFactory")
    public void consumeOrderCreated(OrderCreatedEvent order, @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
        log.info("Received Order: partition {} {}", partition, order);
        paymentService.createFromOrder(order);
    }

//    @KafkaListener(topicPartitions = @TopicPartition(topic = "${kafka.topic.order-created}", partitions = {"0", "1"}), groupId = "${kafka.consumer.payment-group}", containerFactory = "orderKafkaListenerContainerFactory")
//    public void consumeOrderCreated0(@Payload OrderCreatedEvent order, @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
//                                     @Header(KafkaHeaders.RECEIVED_KEY) String key) {
//        log.info("Received Order: key {} partition {} {}", key, partition, order);
//        paymentService.createFromOrder(order);
//    }
}