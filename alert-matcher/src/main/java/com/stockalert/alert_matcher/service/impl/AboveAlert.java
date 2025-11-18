package com.stockalert.alert_matcher.service.impl;

import com.stockalert.alert_matcher.service.IAlertStrategy;
import com.stockalert.alert_matcher.service.payload.TriggeredAlertBatchDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("ABOVE")
@Slf4j
public class AboveAlert implements IAlertStrategy {

    @Override
    public TriggeredAlertBatchDto getTriggeredAlerts(
            RedisTemplate<String, String> redisTemplate,
            String symbol,
            double currentPrice
    ) {
        String activeKey = "alerts:" + symbol + ":ABOVE";
        String processingKey = activeKey + ":processing";

        DefaultRedisScript<List> script = new DefaultRedisScript<>();
        script.setLocation(new ClassPathResource("scripts/claim_alerts_above.lua"));
        script.setResultType(List.class);

        List<String> alerts = redisTemplate.execute(
                script,
                Arrays.asList(activeKey, processingKey),
                String.valueOf(currentPrice)
        );

        if (alerts != null && !alerts.isEmpty()) {
            log.info("⬆ Above triggered {} alerts for {}", alerts.size(), symbol);

            Set<ZSetOperations.TypedTuple<String>> result = new HashSet<>();
            for (String alertId : alerts) {
                result.add(new DefaultTypedTuple<>(alertId, currentPrice));
            }

            return TriggeredAlertBatchDto.builder()
                    .processingKey(processingKey)
                    .alerts(result)
                    .build();
        }

        // Always return non-null
        return TriggeredAlertBatchDto.builder()
                .processingKey(processingKey)
                .alerts(Collections.emptySet())
                .build();
    }
}
