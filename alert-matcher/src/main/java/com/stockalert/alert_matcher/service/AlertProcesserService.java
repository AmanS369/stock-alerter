package com.stockalert.alert_matcher.service;

import com.stockalert.alert_matcher.kafka.ProduceAlertKafka;
import com.stockalert.alert_matcher.config.AlertProperties;
import com.stockalert.alert_matcher.redis.lua.RedisLuaService;
import com.stockalert.alert_matcher.service.payload.AlertResponseDto;
import com.stockalert.alert_matcher.service.payload.TriggeredAlertBatchDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class AlertProcesserService {
    private final AlertProperties alertProperties;


    private final AlertContext alertContext;

    private final RedisTemplate<String, String> redisTemplate;

    private final ProduceAlertKafka produceAlertKafka;

    public void handleTriggeredAlert(AlertResponseDto alert) {
        String symbol = alert.getSymbol();
        double price = alert.getLastPrice();

        alertProperties.getTypeList().forEach(alertType -> {
            IAlertStrategy alertStrategy = alertContext.getStrategy(alertType);
            TriggeredAlertBatchDto triggeredBatch =
                    alertStrategy.getTriggeredAlerts(redisTemplate, symbol, price);

            // no alerts → skip early
            if (triggeredBatch == null || triggeredBatch.getAlerts() == null || triggeredBatch.getAlerts().isEmpty()) {
                return;
            }

            // iterate over all triggered alerts within the batch
            for (ZSetOperations.TypedTuple<String> tuple : triggeredBatch.getAlerts()) {
                produceAlertKafka.publishAlert(
                        symbol,
                        alertType,
                        tuple.getValue(),               // alert ID
                        price,               // alert price
                        triggeredBatch.getProcessingKey() // deletion key
                );
            }
        });
    }



}
