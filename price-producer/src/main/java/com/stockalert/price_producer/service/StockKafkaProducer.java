package com.stockalert.price_producer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockalert.price_producer.payload.StockPriceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j// generates constructor for final fields
public class StockKafkaProducer {

    @Value("${spring.kafka.topic.stock.price}")
    private String stockPriceTopic;

    private final KafkaTemplate<String, StockPriceDto> kafkaTemplate;

    private final ObjectMapper objectMapper;

    public void publish(StockPriceDto tradeData) {
        String key = tradeData.getSymbol();
        kafkaTemplate.send(stockPriceTopic, key, tradeData);
        log.info("✅ Sent to Kafka | Key: {}, Value: {}", key, tradeData);
    }

}