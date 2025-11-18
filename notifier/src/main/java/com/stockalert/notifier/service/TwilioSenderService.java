package com.stockalert.notifier.service;

import com.stockalert.notifier.config.TwilioConfig;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

@Service
public class TwilioSenderService {

    private final TwilioConfig twilioConfig;
    private boolean initialized = false;

    public TwilioSenderService(TwilioConfig twilioConfig) {
        this.twilioConfig = twilioConfig;
    }

    private void initIfNeeded() {
        if (!initialized) {
            Twilio.init(
                    twilioConfig.getAccountSid(),
                    twilioConfig.getAuthToken()
            );
            initialized = true;
        }
    }

    public String sendWhatsappMessage(String to, String msg) {
        initIfNeeded();

        Message message = Message.creator(
                new PhoneNumber("whatsapp:" + to),
                new PhoneNumber(twilioConfig.getFromNumber()),
                msg
        ).create();

        return message.getSid();
    }
}
