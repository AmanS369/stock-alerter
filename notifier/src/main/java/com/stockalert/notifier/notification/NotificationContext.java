package com.stockalert.notifier.notification;

import com.stockalert.notifier.payload.NotificationAlertResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class NotificationContext {

    private final Map<String, INotificationStrategy> strategies;

    public void getStrategy(String channel, NotificationAlertResponseDto notification) {
        INotificationStrategy strategy = strategies.get(channel);

        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported notification channel: " + channel);
        }

         strategy.processNotification(notification);
    }

}
