package com.stockalert.alert_matcher.kafka;

import com.stockalert.alert_matcher.redis.lua.RedisLuaService;
import com.stockalert.alert_matcher.service.payload.PublishNotificationAlertDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProduceAlertKafka {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final RedisLuaService redisLuaService;

    @Value("${spring.kafka.topic.trigger-alert}")
    private String triggerAlertTopic;

    /**
     * Publishes an alert to Kafka and deletes it from Redis on success.
     */
    public void publishAlert(String symbol, String alertType, String alertId, double currentPrice,String processingKey) {

        PublishNotificationAlertDto alert = PublishNotificationAlertDto.builder()
                .symbol(symbol)
                .alertType(alertType)
                .alertId(alertId)
                .currentPrice(currentPrice)
                .build();

        kafkaTemplate.send(triggerAlertTopic, alertId, alert).thenAccept(
                result -> {
                    log.info("✅ Published alert {} ({} @ {}) to Kafka topic {}", alertId, alertType, currentPrice, triggerAlertTopic);
                    redisLuaService.deleteProcessingAlert(processingKey, alertId);
                }
        );
    }
}
