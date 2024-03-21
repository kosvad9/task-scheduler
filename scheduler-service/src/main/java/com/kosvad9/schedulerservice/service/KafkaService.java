package com.kosvad9.schedulerservice.service;

import com.kosvad9.core.EmailMessageEvent;
import com.kosvad9.core.RequestTaskReports;
import com.kosvad9.core.TaskDetail;
import com.kosvad9.core.TaskReport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public void sendReports(List<TaskReport> reports){
        reports.forEach(this::sendReportMessage);
    }

    public void sendReportMessage(TaskReport report){
        List<TaskDetail> completeTasks =  report.tasks().stream()
                .filter(TaskDetail::completeStatus)
                .limit(3)
                .toList();
        List<TaskDetail> unCompleteTasks =  report.tasks().stream()
                .filter(task -> !task.completeStatus())
                .limit(3)
                .toList();
        String header = "";
        String body = "";
        if (!completeTasks.isEmpty() && !unCompleteTasks.isEmpty()){
            header = "Отчет по задачам";
            body = "Выполнено %s задач:\n".formatted(completeTasks.size());
            body += completeTasks.stream().map(TaskDetail::header)
                    .collect(Collectors.joining(";\n"));
            body += "\n";
            body += "Осталось %s невыполненных задач:\n".formatted(unCompleteTasks.size());
            body += unCompleteTasks.stream().map(TaskDetail::header)
                    .collect(Collectors.joining(";\n"));
        } else if (!completeTasks.isEmpty()) {
            header = "За сегодня вы выполнили %s задач".formatted(completeTasks.size());
            body = "Выполненные задачи:\n".formatted(completeTasks.size());
            body += completeTasks.stream().map(TaskDetail::header)
                    .collect(Collectors.joining(";\n"));
        } else if (!unCompleteTasks.isEmpty()) {
            header = "У вас осталось %s невыполненных задач".formatted(unCompleteTasks.size());
            body = "Невыполненные задачи:\n".formatted(unCompleteTasks.size());
            body += unCompleteTasks.stream().map(TaskDetail::header)
                    .collect(Collectors.joining(";\n"));
        }
        EmailMessageEvent emailMessage = EmailMessageEvent.builder()
                .email(report.email())
                .header(header)
                .body(body)
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
