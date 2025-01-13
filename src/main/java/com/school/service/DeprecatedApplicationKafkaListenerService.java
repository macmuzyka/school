package com.school.service;

import com.school.model.dto.ApplicationValidityDTO;
import com.school.model.enums.Validity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class DeprecatedApplicationKafkaListenerService {
    private final ApplicationValidityService applicationValidityService;
    private final Logger log = LoggerFactory.getLogger(DeprecatedApplicationKafkaListenerService.class);

    public DeprecatedApplicationKafkaListenerService(final ApplicationValidityService applicationValidityService) {
        this.applicationValidityService = applicationValidityService;
    }

    @KafkaListener(
            topics = "application-validity",
            groupId = "application-validity",
            containerFactory = "kafkaApplicationValidityListenerContainerFactory"
    )
    public void applicationValidityConsume(ApplicationValidityDTO applicationValidity) {
        Validity validity = applicationValidity.getValidity();
        log.info("[KAFKA LISTENER] => applicationVersion: {} validity: {} message: {}",
                applicationValidity.getLatestVersion(),
                validity,
                applicationValidity.getMessage());
        applicationValidityService.setApplicationValidity(validity);
    }
}
