package com.stockalert.notifier.service;

import com.stockalert.notifier.notification.NotificationContext;
import com.stockalert.notifier.payload.NotificationAlertResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationProcesserService {


    @Value("${alert.strategy}")
    private String strategy;

    private final NotificationContext notificationContext;

    public void processNotification(NotificationAlertResponseDto notification) {

        try {

            notificationContext.getStrategy(strategy,notification);


        } catch (Exception e) {
            log.error("Failed to send WhatsApp notification", e);
        }
    }
}
