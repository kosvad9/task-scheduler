package com.kosvad9.schedulerservice.configuration;

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
    @Value("${kafka.requestTasksTopic}")
    private String REQUEST_TASKS_TOPIC;
    @Bean
    public NewTopic taskReportsTopic(){
        return TopicBuilder.name(REQUEST_TASKS_TOPIC).build();
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
