package com.kosvad9.mailsenderservice.service;

import com.kosvad9.core.EmailMessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    @Value("${mail.from}")
    private String EMAIL_FROM;

    public void sendSimpleEmail(EmailMessageEvent messageEvent){
        log.info("begin send email " + messageEvent.header());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(EMAIL_FROM);
        message.setTo(messageEvent.email());
        message.setSubject(messageEvent.header());
        message.setText(messageEvent.body());
        mailSender.send(message);
    }
}
