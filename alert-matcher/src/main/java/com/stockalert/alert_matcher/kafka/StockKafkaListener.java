package com.stockalert.alert_matcher.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockalert.alert_matcher.service.payload.AlertResponseDto;
import com.stockalert.alert_matcher.service.AlertProcesserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class StockKafkaListener {
    @Value("${spring.kafka.topic.stock-alert}")
    private String stockKafkaTopic;

    private final AlertProcesserService alertProcesserService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Bean
    public KStream<String, String> stockAlertStream(StreamsBuilder builder) {
        KStream<String, String> stream = builder.stream(stockKafkaTopic);
        stream
                .mapValues(value -> {
                    try {
                        return objectMapper.readValue(value, AlertResponseDto.class);
                    } catch (Exception e) {
                        log.info("Failed to parse: " + e.getMessage());
                        return null;
                    }
                })
                .filter((key, dto) -> dto != null)
                .peek((key, dto) -> {
                   // log.info(" Processing Alert " + dto);
                    alertProcesserService.handleTriggeredAlert(dto);
                });

        return stream;

    }

}
