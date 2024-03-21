package com.kosvad9.schedulerservice.listener;

import com.kosvad9.core.ListTaskReports;
import com.kosvad9.core.TaskReport;
import com.kosvad9.schedulerservice.service.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@KafkaListener(topics = "${kafka.taskReportsTopic}")
public class ReportsTopicListener {
    private final KafkaService kafkaService;
    @KafkaHandler
    public void handleReports(ListTaskReports reports){
        try {
            kafkaService.sendReports(reports.taskReportList());
        } catch(Exception e) {
            log.error(e.getMessage());
        }
    }
}
