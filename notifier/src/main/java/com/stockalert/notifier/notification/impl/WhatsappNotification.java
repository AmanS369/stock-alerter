package com.stockalert.notifier.notification.impl;

import com.stockalert.entity.Alert;
import com.stockalert.entity.User;
import com.stockalert.notifier.notification.INotificationStrategy;
import com.stockalert.notifier.payload.NotificationAlertResponseDto;
import com.stockalert.notifier.service.TwilioSenderService;
import com.stockalert.repository.AlertRepository;
import com.stockalert.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service("WHATSAPP_NOTIFIER")
@RequiredArgsConstructor
@Slf4j
@Data
public class WhatsappNotification implements INotificationStrategy {

    private final TwilioSenderService twilioSenderService;

    private final UserRepository userRepository;

    private final AlertRepository alertRepository;

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
                📈 Stock Alert Triggered!
                
                Stock: %s
                Trigger Price: %s
                Current Price: %s
                Time: %s
                """.formatted(
                dto.getSymbol(),
                alert.getTargetPrice(),
                dto.getCurrentPrice(),
                LocalDateTime.now()
        );


        try {
            String sid = twilioSenderService.sendWhatsappMessage(
                    user.get().getPhone(),
                    message
            );

            log.info("WhatsApp sent successfully — SID: {}", sid);

        } catch (Exception e) {
            log.error("Failed to send WhatsApp notification", e);
        }
    }

}

