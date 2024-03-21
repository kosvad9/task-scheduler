package com.kosvad9.schedulerservice.configuration;

import com.kosvad9.core.RequestTaskReports;
import com.kosvad9.schedulerservice.service.KafkaService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
@EnableScheduling
public class SchedulerConfiguration {
    private final KafkaService kafkaService;

    //@Scheduled(fixedDelay = 3000000L)
    //@Scheduled(cron = "10 * * * * *")
    @Scheduled(cron = "${cron.expression}")
    public void sendTaskReport(){
        kafkaService.sendRequestTaskReports(
                new RequestTaskReports(LocalDateTime.now().minusHours(24L),
                                                LocalDateTime.now()));
    }
}
