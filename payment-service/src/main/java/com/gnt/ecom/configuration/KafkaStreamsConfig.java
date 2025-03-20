package com.gnt.ecom.configuration;

import com.gnt.ecom.order.event.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;
import java.util.Properties;

@Slf4j
@Configuration
public class KafkaStreamsConfig {

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
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class);
        props.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG,
                LogAndContinueExceptionHandler.class);
        props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, "2");
        props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, "1");

        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, OrderCreatedEvent> stream = builder.stream(
                orderCreatedTopic,
                Consumed.with(
                        Serdes.String(),
                        new JsonSerde<>(OrderCreatedEvent.class)
                )
        );

        stream.filter((key, value) -> {
            if (value == null) {
                log.warn("Received null event for key: {}", key);
                return false;
            }
            return true;
        }).foreach((key, event) -> {
            try {
                log.info("Payment-service stream processing: {}", event);
            } catch (Exception e) {
                log.error("Error processing event: {}", event, e);
            }
        });

        // Process the stream for aggregation
        KTable<Windowed<String>, Double> totalSalesByCurrency = stream
                .filter((key, value) -> value != null) // Additional null check
                .groupBy((key, event) -> event.getUserCurrency())
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(20)))
                .aggregate(
                        () -> 0.0,
                        (key, event, aggregate) -> aggregate + event.getTotalLocalPrice().doubleValue(),
                        Materialized.with(Serdes.String(), Serdes.Double())
                );

        totalSalesByCurrency.toStream().foreach((windowedCurrency, total) ->
                log.info("Total sales for {} in window [{} - {}]: {}",
                        windowedCurrency.key(),
                        windowedCurrency.window().startTime(),
                        windowedCurrency.window().endTime(),
                        total));

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

        streams.start();
        return streams;
    }
}