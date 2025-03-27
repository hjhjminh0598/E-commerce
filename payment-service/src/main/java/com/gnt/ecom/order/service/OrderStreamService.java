package com.gnt.ecom.order.service;

import com.gnt.ecom.order.event.OrderCreatedEvent;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.Stores;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.Map;

import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderStreamService {

    @Value("${kafka.topic.order-created}")
    private String orderCreatedTopic;

    private final Properties kafkaStreamsProperties;

    private final SpecificAvroSerde<OrderCreatedEvent> orderCreatedSerde;

    private KafkaStreams streams;

    private static final String SALES_BY_CURRENCY_STORE = "sales-by-currency-store";

    @PostConstruct
    public void startStreams() {
        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, OrderCreatedEvent> stream = builder.stream(
                orderCreatedTopic,
                Consumed.with(Serdes.String(), orderCreatedSerde)
        );

        stream.filter((key, value) -> value != null)
                .foreach((key, event) -> log.info("Processing order event: {}", event));

        KeyValueBytesStoreSupplier storeSupplier = Stores.persistentKeyValueStore(SALES_BY_CURRENCY_STORE);

        KTable<String, Double> totalSalesByCurrency = stream
                .filter((key, value) -> value != null)
                .groupBy((key, event) -> event.getUserCurrency())
                .aggregate(
                        () -> 0.0,
                        (key, event, aggregate) -> aggregate + new BigDecimal(event.getTotalLocalPrice()).doubleValue(),
                        Materialized.<String, Double>as(storeSupplier)
                                .withKeySerde(Serdes.String())
                                .withValueSerde(Serdes.Double())
                );

        totalSalesByCurrency.toStream().foreach((currency, total) -> log.info("Total sales for {}: {}", currency, total));

        streams = new KafkaStreams(builder.build(), kafkaStreamsProperties);
        streams.start();
    }

    @PreDestroy
    public void cleanup() {
        if (streams != null) {
            streams.close();
        }
    }

    public Map<String, Double> getSalesByCurrency(String[] currencies) {
        if (streams == null || streams.state() != KafkaStreams.State.RUNNING) {
            return Map.of();
        }

        ReadOnlyKeyValueStore<String, Double> store = streams.store(
                StoreQueryParameters.fromNameAndType(SALES_BY_CURRENCY_STORE, QueryableStoreTypes.keyValueStore())
        );

        if (store == null) {
            return Map.of();
        }

        Map<String, Double> result = new LinkedHashMap<>();
        if (ArrayUtils.isNotEmpty(currencies)) {
            for (String currency : currencies) {
                Double value = store.get(currency);
                result.put(currency, value != null ? value : 0);
            }
        } else {
            store.all().forEachRemaining(keyValue -> result.put(keyValue.key, keyValue.value));
        }

        return result;
    }
}