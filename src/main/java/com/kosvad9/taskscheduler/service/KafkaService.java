package com.kosvad9.taskscheduler.service;

import com.kosvad9.core.EmailMessageEvent;
import com.kosvad9.core.TaskReport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaService {
    private final KafkaTemplate<String, EmailMessageEvent> kafkaTemplateEmail;
    private final KafkaTemplate<String, List<TaskReport>> kafkaTemplateReports;

    @Value("${kafka.emailTopic}")
    private String EMAIL_TOPIC;

    @Value("${kafka.taskReportsTopic}")
    private String TASK_REPORTS_TOPIC;

    public void sendRegistrationMessage(String email){
        EmailMessageEvent emailMessage = EmailMessageEvent.builder()
                .email(email)
                .header("Регистрация")
                .body("Спасибо за регистрацию на сервисе планировщика задач!")
                .build();
        var result = kafkaTemplateEmail.send(EMAIL_TOPIC, emailMessage);
        result.whenComplete((sendResult, exception) -> {
            if (exception != null){
                log.error(exception.getMessage());
            }else {
                log.info(sendResult.getRecordMetadata().toString());
            }
        });
    }

    public void sendTaskReports(List<TaskReport> reports){
        var result = kafkaTemplateReports.send(TASK_REPORTS_TOPIC, reports);
        result.whenComplete((sendResult, exception) -> {
            if (exception != null){
                log.error(exception.getMessage());
            }else {
                log.info(sendResult.getRecordMetadata().toString());
            }
        });
    }
}
