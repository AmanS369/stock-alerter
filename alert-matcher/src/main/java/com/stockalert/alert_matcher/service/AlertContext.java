package com.stockalert.alert_matcher.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class AlertContext {

    private final Map<String, IAlertStrategy> stringIAlertStrategyMap;

    public IAlertStrategy getStrategy(String strategy) {
        IAlertStrategy alertStrategy = stringIAlertStrategyMap.get(strategy);
        if (alertStrategy == null) {
            log.warn(" No strategy found for {}", strategy);
        }
        return alertStrategy;
    }
}
