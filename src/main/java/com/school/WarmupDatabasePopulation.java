package com.school;

import com.school.repository.SchoolRepository;
import com.schoolmodel.model.*;
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

    private static final List<Subject> subjects = List.of(
            new Subject("Math"),
            new Subject("History"),
            new Subject("Art"),
            new Subject("English")
    );

    public WarmupDatabasePopulation(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
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
        List<SchoolClass> classes = new ArrayList<>();
        classes.add(new SchoolClass("Class ONE", subjects));
        classes.add(new SchoolClass("Class TWO", subjects));
        classes.add(new SchoolClass("Class THREE", subjects));
        School saved = schoolRepository.save(new School("SCHOOL ONE", classes));
        log.info("Application warmup school saved to database!");
        log.debug("Saved School: {}", saved);
    }
}
