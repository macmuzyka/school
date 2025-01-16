package com.school;

import com.school.service.ProjectVersionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class WarmupRoadmapFetchFromCentral implements ApplicationListener<ApplicationReadyEvent> {
    private final KafkaTemplate<String, String> roadmapFetchKafkaTemplate;
    private final ProjectVersionService projectVersionService;
    private final Logger log = LoggerFactory.getLogger(WarmupRoadmapFetchFromCentral.class);

    public WarmupRoadmapFetchFromCentral(KafkaTemplate<String, String> roadmapFetchKafkaTemplate, ProjectVersionService projectVersionService) {
        this.roadmapFetchKafkaTemplate = roadmapFetchKafkaTemplate;
        this.projectVersionService = projectVersionService;
    }

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        waitForApplicationIsAbleToConsumeKafkaMessages();
    }

    private void waitForApplicationIsAbleToConsumeKafkaMessages() {
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        fetchRoadmapFromCentral();
    }

    private void fetchRoadmapFromCentral() {
        log.info("Sending fetch request via kafka");
        roadmapFetchKafkaTemplate.send("application-version-fetch", projectVersionService.getCurrentProjectVersion().getVersion());
    }
}
