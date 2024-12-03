package com.school.configuration;

import com.school.service.EnvironmentService;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {
    private final EnvironmentService environmentService;
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;
    private final Logger log = LoggerFactory.getLogger(KafkaTopicConfig.class);

    public KafkaTopicConfig(EnvironmentService environmentService) {
        this.environmentService = environmentService;
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        if (environmentService.currentProfileIsDevel()) {
            log.warn("On purpose decreasing request timeout to Kafka message broker in devel profile (for example, when Kafka is not installed");
            configs.put(AdminClientConfig.DEFAULT_API_TIMEOUT_MS_CONFIG, 1000);
            configs.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, 1000);
        }
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic mathGradeSupplyingTopic() {
        return new NewTopic("math-grade-supplier", 1, (short) 1);
    }

    @Bean
    public NewTopic historyGradeSupplyingTopic() {
        return new NewTopic("history-grade-supplier", 1, (short) 1);
    }

    @Bean
    public NewTopic englishGradeSupplyingTopic() {
        return new NewTopic("english-grade-supplier", 1, (short) 1);
    }

    @Bean
    public NewTopic artGradeSupplyingTopic() {
        return new NewTopic("art-grade-supplier", 1, (short) 1);
    }


}