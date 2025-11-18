package com.stockalert.notifier.config;

import com.stockalert.notifier.payload.EmailBodyDto;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class EmailConfig {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public void sendSimpleMail(EmailBodyDto details) {

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(sender);
            helper.setTo(details.getRecipient());
            helper.setSubject(details.getSubject());


            helper.setText(details.getMsgBody(), true);

            javaMailSender.send(mimeMessage);

            log.info("Mail Sent Successfully...");
        } catch (Exception e) {
            log.error("Something went wrong while sending email", e);
        }
    }
}
