package com.school;

import com.school.service.ProjectVersionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class WarmupRoadmapFetchFromCentral implements ApplicationListener<ApplicationStartedEvent> {
    private final KafkaTemplate<String, String> roadmapFetchKafkaTemplate;
    private final ProjectVersionService projectVersionService;
    private final Logger log = LoggerFactory.getLogger(WarmupRoadmapFetchFromCentral.class);

    public WarmupRoadmapFetchFromCentral(KafkaTemplate<String, String> roadmapFetchKafkaTemplate, ProjectVersionService projectVersionService) {
        this.roadmapFetchKafkaTemplate = roadmapFetchKafkaTemplate;
        this.projectVersionService = projectVersionService;
    }

    @Override
    public void onApplicationEvent(final ApplicationStartedEvent event) {
        fetchRoadmapFromCentral();
    }

    private void fetchRoadmapFromCentral() {
        log.info("Sending fetch request via kafka");
        roadmapFetchKafkaTemplate.send("application-version-fetch", projectVersionService.getCurrentProjectVersion().getVersion());
    }
}
