package com.gnt.ecom.configuration;

import com.gnt.ecom.order.event.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;
import java.util.Properties;

@Slf4j
@Configuration
public class OrderStreamsConfig {

    @Value("${kafka.topic.order-created}")
    private String orderCreatedTopic;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.streams.application-id}")
    private String paymentStreamAppId;

    @Bean(destroyMethod = "close")
    public KafkaStreams kafkaStreams() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, paymentStreamAppId);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, OrderCreatedEvent> stream = builder.stream(orderCreatedTopic,
                Consumed.with(Serdes.String(), new JsonSerde<>(OrderCreatedEvent.class)));

        stream.foreach((key, event) -> {
            try {
                log.info("Payment-service stream processing: {}", event);
            } catch (Exception e) {
                log.error("Error processing event: {}", event, e);
            }
        });

        KTable<Windowed<String>, Double> totalSalesByCurrency = stream
                .groupBy((key, event) -> event.getUserCurrency())
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(20)))
                .aggregate(
                        () -> 0.0,
                        (key, event, aggregate) -> aggregate + event.getTotalLocalPrice().doubleValue(),
                        Materialized.with(Serdes.String(), Serdes.Double())
                );

        totalSalesByCurrency.toStream().foreach((currency, total) ->
                log.info("Total sales for {}: {}", currency, total));

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();

        return streams;
    }
}