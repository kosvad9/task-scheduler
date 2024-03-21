package com.kosvad9.schedulerservice.service;

import com.kosvad9.core.EmailMessageEvent;
import com.kosvad9.core.RequestTaskReports;
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
    private final KafkaTemplate<String, RequestTaskReports> kafkaTemplateRequest;

    @Value("${kafka.emailTopic}")
    private String EMAIL_TOPIC;

    @Value("${kafka.requestTasksTopic}")
    private String REQUEST_TASKS_TOPIC;

    public void sendReportMessage(List<TaskReport> reports){
        EmailMessageEvent emailMessage = EmailMessageEvent.builder()
               // .email(email)
                .header("Выполнено задач")
                .body("осталось задач N")
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

    public void sendRequestTaskReports(RequestTaskReports request){
        var result = kafkaTemplateRequest.send(REQUEST_TASKS_TOPIC, request);
        result.whenComplete((sendResult, exception) -> {
            if (exception != null){
                log.error(exception.getMessage());
            }else {
                log.info(sendResult.getRecordMetadata().toString());
            }
        });
    }
}
