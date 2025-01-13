package com.school;

import com.school.repository.SchoolRepository;
import com.school.model.entity.School;
import com.school.model.entity.SchoolClass;
import com.school.service.ClassService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class WarmupDatabasePopulation implements ApplicationListener<ApplicationStartedEvent> {
    private final SchoolRepository schoolRepository;
    private final ClassService classService;
    private static final Logger log = LoggerFactory.getLogger(WarmupDatabasePopulation.class);

    public WarmupDatabasePopulation(SchoolRepository schoolRepository, ClassService classService) {
        this.schoolRepository = schoolRepository;
        this.classService = classService;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        log.info("Warmup database population");
        if (schoolRepository.findAll().isEmpty()) {
            addSchoolWithClassOnWarmup();
        } else {
            log.info("Warmup population not needed!");
        }
    }

    private void addSchoolWithClassOnWarmup() {
        School savedSchool = schoolRepository.save(new School("SCHOOL ONE", new ArrayList<>()));
        savedSchool.getSchoolClasses().add(firstWarmupClass());
        schoolRepository.save(savedSchool);

        log.info("Application warmup school saved to database!");
        log.debug("Saved School: {}", savedSchool);
    }

    private SchoolClass firstWarmupClass() {
        return classService.createNewClassWithAssignedSubjects();
    }
}
