package com.school;

import com.school.repository.SchoolRepository;
import com.school.service.ApplicationWarmupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class WarmupDatabasePopulation implements ApplicationListener<ApplicationStartedEvent> {
    private final ApplicationWarmupService applicationWarmupService;
    private final SchoolRepository schoolRepository;
    private static final Logger log = LoggerFactory.getLogger(WarmupDatabasePopulation.class);

    public WarmupDatabasePopulation(ApplicationWarmupService applicationWarmupService,
                                    SchoolRepository schoolRepository) {
        this.applicationWarmupService = applicationWarmupService;
        this.schoolRepository = schoolRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        log.info("Warmup database population");
        if (schoolRepository.findAll().isEmpty()) {
            applicationWarmupService.addSchoolWithClassOnWarmup();
        } else {
            log.info("Warmup population not needed!");
        }
    }
}
