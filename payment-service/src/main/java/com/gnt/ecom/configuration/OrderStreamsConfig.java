package com.gnt.ecom.configuration;

import com.gnt.ecom.order.event.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;

@Slf4j
@Configuration
@EnableKafkaStreams
public class OrderStreamsConfig {

    @Value("${kafka.topic.order-created}")
    private String orderCreatedTopic;

    @Bean
    public KStream<String, OrderCreatedEvent> orderStream(StreamsBuilder streamsBuilder) {
        KStream<String, OrderCreatedEvent> stream = streamsBuilder
                .stream(orderCreatedTopic, Consumed.with(Serdes.String(), new JsonSerde<>(OrderCreatedEvent.class)));

        stream.foreach((key, event) -> {
            System.out.println(event);
            log.info("Payment-service stream processing OrderCreatedEvent: {}", event);
        });

        stream.mapValues(event -> {
            log.info("Transforming event for payment processing: {}", event);
            return event; // Modify event as needed, e.g., add payment status
        }).to("payment-processed-topic"); // Define this topic if needed

        return stream;
    }
}