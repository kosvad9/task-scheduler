package com.kosvad9.taskscheduler.listener;

import com.kosvad9.core.RequestTaskReports;
import com.kosvad9.taskscheduler.service.KafkaService;
import com.kosvad9.taskscheduler.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@KafkaListener(topics = "${kafka.requestTasksTopic}")
public class RequestTopicListener {
    private final KafkaService kafkaService;
    private final TaskService taskService;

    @KafkaHandler
    public void handleRequest(RequestTaskReports request){
        try {
            kafkaService.sendTaskReports(taskService.getTasksForReport(request.from(),request.to()));
        } catch(Exception e) {
            log.error(e.getMessage());
        }
    }
}
