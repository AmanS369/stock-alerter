package com.stockalert.alert_api.service;

import com.stockalert.entity.Alert;
import com.stockalert.alert_api.payload.CreateAlertDto;
import com.stockalert.repository.AlertRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;
    private final RedisService redisService;
    @Transactional
    public void createAlert(CreateAlertDto createAlertDto){

        Alert newAlert = Alert.builder()
                .symbol(createAlertDto.getSymbol())
                .status("ACTIVE")
                .userId(createAlertDto.getUserId())
                .targetPrice(createAlertDto.getPrice())
                .condition(createAlertDto.getCondition())
                .createdAt(LocalDateTime.now())
                .build();

        alertRepository.save(newAlert);
       redisService.createRedisAlert(newAlert);

    }
}
