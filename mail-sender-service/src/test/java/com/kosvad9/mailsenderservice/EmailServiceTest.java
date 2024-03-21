package com.kosvad9.mailsenderservice;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@RequiredArgsConstructor
@SpringBootTest(classes = TestRunner.class)
public class EmailServiceTest {

    public final ApplicationContext context;
    public final JavaMailSender mailSender;

    @Test
    public void sendEmail(){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("");
        message.setTo("");
        message.setSubject("subject");
        message.setText("test message");
        mailSender.send(message);
    }
}
