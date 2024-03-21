package com.kosvad9.mailsenderservice.listener;

import com.kosvad9.core.EmailMessageEvent;
import com.kosvad9.mailsenderservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@KafkaListener(topics = "${kafka.emailTopic}")
public class EmailTopicListener {
    private final EmailService emailService;
    @KafkaHandler
    public void handle(EmailMessageEvent emailMessage){
        try {
            emailService.sendSimpleEmail(emailMessage);
        } catch (MailException e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
