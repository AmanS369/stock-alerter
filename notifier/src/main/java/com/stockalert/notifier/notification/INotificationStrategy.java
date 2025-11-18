package com.stockalert.notifier.notification;

import com.stockalert.notifier.payload.NotificationAlertResponseDto;

public interface INotificationStrategy {
    public void processNotification(NotificationAlertResponseDto dto);
}
