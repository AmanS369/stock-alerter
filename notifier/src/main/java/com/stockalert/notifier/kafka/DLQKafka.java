package com.stockalert.notifier.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DLQKafka {


    @Value("${spring.kafka.topic.dlq.alert}")
    private String dlqKafkaTopic;

    private final KafkaTemplate<String, String> kafkaTemplate; // ADD THIS
    private final ObjectMapper objectMapper = new ObjectMapper();
    public void sendToDlq(String key, Object payload, String reason) {
        try {
            String dlqMessage = objectMapper.writeValueAsString(new DlqMessage(
                    payload,
                    reason,
                    System.currentTimeMillis()
            ));

            kafkaTemplate.send(dlqKafkaTopic, key, dlqMessage);
            log.error("Message sent to DLQ | Topic: {} | Key: {} | Reason: {}",
                    dlqKafkaTopic, key, reason);

        } catch (Exception e) {
            log.error("Failed to send message to DLQ: {}", e.getMessage(), e);
        }
    }
    private record DlqMessage(
            Object originalPayload,
            String failureReason,
            long timestamp
    ) {}
}
