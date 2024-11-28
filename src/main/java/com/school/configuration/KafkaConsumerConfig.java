package com.school.configuration;

import com.school.service.GradeService;
import com.schoolmodel.model.dto.GradeDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerConfig.class);
    private final GradeService gradeService;

    public KafkaConsumerConfig(GradeService service) {
        this.gradeService = service;
    }

    @Bean
    public ConsumerFactory<String, GradeDTO> gradeConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "grades");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(GradeDTO.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, GradeDTO> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, GradeDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(gradeConsumerFactory());
        return factory;
    }

    @KafkaListener(topics = {"math-grade-supplier", "history-grade-supplier", "english-grade-supplier", "art-grade-supplier"}, groupId = "grades")
    public void listenGroupGrades(GradeDTO grade) {
        log.info("Received Message in group grades: " + grade);
        gradeService.addGrade(grade.getValue(), grade.getSubjectId(), grade.getStudentId());
    }
}
