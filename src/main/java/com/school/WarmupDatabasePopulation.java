package com.school;

import com.school.repository.SchoolRepository;
import com.schoolmodel.model.entity.School;
import com.schoolmodel.model.entity.SchoolClass;
import com.schoolmodel.model.entity.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class WarmupDatabasePopulation implements ApplicationListener<ApplicationStartedEvent> {
    private static final Logger log = LoggerFactory.getLogger(WarmupDatabasePopulation.class);
    @Value("#{'${available.subjects}'.split(',')}")
    private List<String> subjects;
    private final SchoolRepository schoolRepository;

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
        SchoolClass class1 = new SchoolClass("Class 1", subjects.stream().map(Subject::new).toList());
        SchoolClass class2 = new SchoolClass("Class 2", subjects.stream().map(Subject::new).toList());
        SchoolClass class3 = new SchoolClass("Class 3", subjects.stream().map(Subject::new).toList());

        ArrayList<SchoolClass> classes = new ArrayList<>();
        classes.add(class1);
        classes.add(class2);
        classes.add(class3);

        School saved = schoolRepository.save(new School("SCHOOL ONE", classes));

        log.info("Application warmup school saved to database!");
        log.debug("Saved School: {}", saved);
    }
}
