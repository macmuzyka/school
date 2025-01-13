package com.school.configuration;

import com.school.model.dto.FeedbackDTO;
import com.school.model.dto.GradeDTO;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTemplatesProducersConfig {
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public ProducerFactory<String, GradeDTO> gradeProducerFactory() {
        return new DefaultKafkaProducerFactory<>(provideBasicKafkaProducerConfig(false));
    }

    @Bean
    public KafkaTemplate<String, GradeDTO> gradeKafkaTemplate() {
        return new KafkaTemplate<>(gradeProducerFactory());
    }

    @Bean
    public ProducerFactory<String, FeedbackDTO> feedbackProducerFactory() {
        return new DefaultKafkaProducerFactory<>(provideBasicKafkaProducerConfig(false));
    }

    @Bean
    public KafkaTemplate<String, FeedbackDTO> feedbackKafkaTemplate() {
        return new KafkaTemplate<>(feedbackProducerFactory());
    }

    @Bean
    public ProducerFactory<String, String> roadmapFetchProducerFactory() {
        return new DefaultKafkaProducerFactory<>(provideBasicKafkaProducerConfig(true));
    }

    @Bean
    public KafkaTemplate<String, String> roadmapFetchKafkaTemplate() {
        return new KafkaTemplate<>(roadmapFetchProducerFactory());
    }

    @Bean
    public ProducerFactory<String, String> applicationDeprecatedFactory() {
        return new DefaultKafkaProducerFactory<>(provideBasicKafkaProducerConfig(true));
    }

    @Bean
    public KafkaTemplate<String, String> applicationDeprecatedKafkaTemplate() {
        return new KafkaTemplate<>(applicationDeprecatedFactory());
    }

    private Map<String, Object> provideBasicKafkaProducerConfig(boolean useStringValueSerializer) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        if (useStringValueSerializer) {
            configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        } else {
            configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        }
        return configProps;
    }
}
