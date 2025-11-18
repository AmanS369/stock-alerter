package com.stockalert.notifier.notification.impl;

import com.stockalert.entity.Alert;
import com.stockalert.entity.User;
import com.stockalert.notifier.config.EmailConfig;
import com.stockalert.notifier.notification.INotificationStrategy;
import com.stockalert.notifier.payload.EmailBodyDto;
import com.stockalert.notifier.payload.NotificationAlertResponseDto;
import com.stockalert.repository.AlertRepository;
import com.stockalert.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service("EMAIL_NOTIFIER")
@RequiredArgsConstructor
public class EmailNotifier implements INotificationStrategy {
    private final AlertRepository alertRepository;

    private final UserRepository userRepository;

    private final EmailConfig emailConfig;
    @Override
    public void processNotification(NotificationAlertResponseDto dto) {

        Optional<Alert> alertOpt = alertRepository.findById(Long.valueOf(dto.getAlertId()));

        if (alertOpt.isEmpty()) {
            log.warn(" No alert found for id {}", dto.getAlertId());
            return;
        }

        Alert alert = alertOpt.get();
        Optional<User> user = userRepository.findById(alert.getUserId());
        log.info(" Alert triggered — ID: {}", alert.getId());

        alert.setTriggeredAt(LocalDateTime.now());
        alertRepository.save(alert);


        String message = """
        <div style="font-family: Arial, sans-serif; font-size:14px; color:#111;">
            <h2 style="color:#1a73e8; margin-bottom:10px;">📈 Stock Alert Triggered!</h2>
            
            <p><strong>Stock:</strong> %s</p>
            <p><strong>Trigger Price:</strong> %s</p>
            <p><strong>Current Price:</strong> %s</p>
            <p><strong>Time:</strong> %s</p>
            
            <hr style="border:none;border-top:1px solid #ddd;margin:20px 0;">
            
            <p>Stay alert,</p>
            <p><strong>StockAlert System</strong></p>
        </div>
        """.formatted(
                dto.getSymbol(),
                alert.getTargetPrice(),
                dto.getCurrentPrice(),
                LocalDateTime.now()
        );


        EmailBodyDto newEmail = EmailBodyDto.builder()
                .recipient(user.get().getEmail())
                .msgBody(message)
                .subject("[URGENT]:Stock Alert")
                .build();
        try {
            emailConfig.sendSimpleMail(newEmail);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
