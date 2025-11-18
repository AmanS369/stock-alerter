package com.stockalert.notifier.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockalert.notifier.payload.NotificationAlertResponseDto;
import com.stockalert.notifier.service.NotificationProcesserService;
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

    @Value("${spring.kafka.topic.trigger-alert}")
    private String triggerKafkaTopic;

    @Value("${spring.kafka.topic.dlq.max-attempt}")
    private int maxAttempt;

    private final NotificationProcesserService notificationProcesserService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final DLQKafka dlqKafka;

    @Bean
    public KStream<String, String> stockAlertStream(StreamsBuilder builder) {
        log.info("Initializing Kafka Stream listener for topic: {}", triggerKafkaTopic);

        KStream<String, String> stream = builder.stream(triggerKafkaTopic);

        stream
                .peek((key, value) -> log.debug("Received message | Key: {}, Value: {}", key, value))
                .mapValues(value -> {
                    try {
                        NotificationAlertResponseDto dto = objectMapper.readValue(value, NotificationAlertResponseDto.class);
                        log.debug("Parsed DTO successfully: {}", dto);
                        return dto;
                    } catch (Exception e) {
                        log.error("Error parsing message value: {} | Exception: {}", value, e.getMessage());
                        return null;
                    }
                })
                .filter((key, notification) -> {
                    boolean valid = notification != null;
                    if (!valid) {
                        log.warn("Filtered out null or invalid DTO for key: {}", key);
                    }
                    return valid;
                })
                .foreach((key, dto) -> {
                    try {
                        notificationProcesserService.processNotification(dto);
                        log.info("Processed notification successfully for key: {}", key);
                    } catch (Exception e) {
                        log.error("Failed to process notification for key: {} | Exception: {}", key, e.getMessage(), e);
                    }
                });

        log.info("Kafka Stream listener configured successfully for topic: {}", triggerKafkaTopic);
        return stream;
    }

    private void processWithRetry(String key ,NotificationAlertResponseDto notificationDto){
        for(int attempt =1 ; attempt <= maxAttempt ; attempt++){
            try {
                notificationProcesserService.processNotification(notificationDto);
                log.info("Processed notification successfully for key: {}", key);
                return;
            } catch (Exception e) {
                log.error("Failed to process notification for key: {} | Exception: {}", key, e.getMessage(), e);
                if (attempt < maxAttempt) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        log.error("Retry sleep interrupted for alertId: {}", notificationDto.getAlertId());
                        break;
                    }
                } else {
                    log.error("All {} attempts failed for alertId: {}. Sending to DLQ.",
                            maxAttempt, notificationDto.getAlertId());
                    dlqKafka.sendToDlq(key, notificationDto, "MAX_RETRIES_EXCEEDED: " + e.getMessage());
                }
            }

        }
    }
}
