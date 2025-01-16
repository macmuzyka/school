package com.school.configuration;

import com.school.deserializer.ApplicationValidityDTODeserializer;
import com.school.model.dto.ApplicationValidityDTO;
import com.school.model.dto.GradeDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, GradeDTO> kafkaGradeListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, GradeDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(buildConsumerConfigPropertiesForGroupId("grades", GradeDTO.class, null));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Map<String, List<String>>> kafkaRoadmapListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Map<String, List<String>>> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(buildConsumerConfigPropertiesForGroupId("roadmap-fetch", Map.class, null));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ApplicationValidityDTO> kafkaApplicationValidityListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ApplicationValidityDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(buildConsumerConfigPropertiesForGroupId("application-validity", ApplicationValidityDTO.class, ApplicationValidityDTODeserializer.class));
        return factory;
    }

    private <T, D> ConsumerFactory<String, T> buildConsumerConfigPropertiesForGroupId(String groupId, Class<T> outcome, Class<D> deserializer) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        //FIXME: need to add this in school project, but no in informer project?
        // OTHERWISE: Caused by: java.lang.IllegalArgumentException: The class 'kotlin.collections.EmptyMap'
        // is not in the trusted packages: [java.util, java.lang, java.util.*].
        // If you believe this class is safe to deserialize, please provide its name.
        // If the serialization is only done by a trusted source, you can also enable trust all (*).
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        if (deserializer != null) {
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
            return new DefaultKafkaConsumerFactory<>(props);
        } else {
            return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(outcome));
        }
    }
}