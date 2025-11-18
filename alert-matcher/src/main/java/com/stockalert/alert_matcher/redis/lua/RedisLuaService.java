package com.stockalert.alert_matcher.redis.lua;

import io.lettuce.core.RedisCommandExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.type.SerializationException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisLuaService {
    private final RedisTemplate<String, String> redisTemplate;

    public void deleteProcessingAlert(String processingKey, String alertId) {
        try {
            DefaultRedisScript<Long> script = new DefaultRedisScript<>();
            script.setLocation(new ClassPathResource("scripts/delete_processing_alerts.lua"));
            script.setResultType(Long.class);

            Long removed = redisTemplate.execute(
                    script,
                    Collections.singletonList(processingKey),
                    alertId
            );

            if (removed != null && removed > 0) {
                log.info("Deleted {} from {}", alertId, processingKey);
            } else {
                log.warn("Tried to delete {} but not found in {}", alertId, processingKey);
            }

        } catch (Exception e) {
            log.error("Unexpected error while deleting {} from {}: {}", alertId, processingKey, e.getMessage(), e);
        }
    }

}
