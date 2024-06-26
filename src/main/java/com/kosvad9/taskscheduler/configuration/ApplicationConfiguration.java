package com.kosvad9.taskscheduler.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class ApplicationConfiguration {

    @Value("${kafka.emailTopic}")
    private String EMAIL_TOPIC;

    @Value("${kafka.taskReportsTopic}")
    private String TASK_REPORTS_TOPIC;

    @Bean
    public NewTopic emailMessagesTopic(){
        return TopicBuilder.name(EMAIL_TOPIC).build();
    }

    @Bean
    public NewTopic taskReportsTopic(){
        return TopicBuilder.name(TASK_REPORTS_TOPIC).build();
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> concurrentKafkaListenerContainerFactoryConfig(
            ConcurrentKafkaListenerContainerFactory<String, Object> factory,
            KafkaTemplate<String, Object> kafkaTemplate){
        DefaultErrorHandler defaultErrorHandler =
                new DefaultErrorHandler(new DeadLetterPublishingRecoverer(kafkaTemplate),
                        new FixedBackOff(3000, 3));
        defaultErrorHandler.addNotRetryableExceptions(Exception.class);
        factory.setCommonErrorHandler(defaultErrorHandler);
        return factory;
    }
}
