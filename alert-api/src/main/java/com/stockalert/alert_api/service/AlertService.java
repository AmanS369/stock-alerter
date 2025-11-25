package com.stockalert.alert_api.service;

import com.stockalert.alert_api.payload.AlertDto;
import com.stockalert.entity.Alert;
import com.stockalert.alert_api.payload.CreateAlertDto;
import com.stockalert.repository.AlertRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public List<AlertDto> getAllAlert(){
        List<Alert> allAlert = alertRepository.findAll();
        List<AlertDto> alertDtoList = new ArrayList<>();
        allAlert.forEach(alert->{
            alertDtoList.add( AlertDto.builder()
                            .symbol(alert.getSymbol())
                            .condition(alert.getCondition())
                            .status(alert.getStatus())
                            .triggeredAt(alert.getTriggeredAt())
                            .notifiedAt(alert.getNotifiedAt())
                            .createdAt(alert.getCreatedAt())
                    .build());
        });

        return alertDtoList;
    }


}
