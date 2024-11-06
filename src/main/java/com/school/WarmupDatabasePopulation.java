package com.school;

import com.school.configuration.ApplicationConfig;
import com.school.repository.SchoolClassRepository;
import com.school.repository.SchoolRepository;
import com.school.repository.SubjectRepository;
import com.schoolmodel.model.entity.School;
import com.schoolmodel.model.entity.SchoolClass;
import com.schoolmodel.model.entity.Subject;
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
    private final SubjectRepository subjectRepository;
    private final ApplicationConfig applicationConfig;

    public WarmupDatabasePopulation(SchoolRepository schoolRepository, SchoolClassRepository schoolClassRepository, SubjectRepository subjectRepository, ApplicationConfig applicationConfig) {
        this.schoolRepository = schoolRepository;
        this.schoolClassRepository = schoolClassRepository;
        this.subjectRepository = subjectRepository;
        this.applicationConfig = applicationConfig;
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
        return schoolClassRepository.save(
                new SchoolClass("Class 1", applicationConfig.getAvailableSubjects().stream()
                        .map(sub -> subjectRepository.save(new Subject(sub)))
                        .toList()
                )
        );
    }
}
