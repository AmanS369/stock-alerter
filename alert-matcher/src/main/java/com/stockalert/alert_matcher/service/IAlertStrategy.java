package com.stockalert.alert_matcher.service;

import com.stockalert.alert_matcher.service.payload.TriggeredAlertBatchDto;
import org.springframework.data.redis.core.RedisTemplate;

public interface IAlertStrategy {
    TriggeredAlertBatchDto getTriggeredAlerts(
            RedisTemplate<String, String> redisTemplate,
            String symbol,
            double currentPrice
    );

}
