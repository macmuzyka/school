package com.school;

import com.school.configuration.ApplicationConfig;
import com.school.repository.SchoolClassRepository;
import com.school.repository.SchoolRepository;
import com.school.repository.SubjectRepository;
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
    private static final Logger log = LoggerFactory.getLogger(WarmupDatabasePopulation.class);
    private final SchoolRepository schoolRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final ClassService classService;

    public WarmupDatabasePopulation(SchoolRepository schoolRepository, SchoolClassRepository schoolClassRepository, ClassService classService) {
        this.schoolRepository = schoolRepository;
        this.schoolClassRepository = schoolClassRepository;
        this.classService = classService;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        log.info("Application warmup...");
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
        return schoolClassRepository.save(classService.createNewClassWithAssignedSubjects());
    }
}
