package com.school;

import com.school.repository.SchoolClassRepository;
import com.school.repository.SchoolRepository;
import com.school.repository.SubjectRepository;
import com.schoolmodel.model.entity.School;
import com.schoolmodel.model.entity.SchoolClass;
import com.schoolmodel.model.entity.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.config.ScheduledTaskHolder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class WarmupDatabasePopulation implements ApplicationListener<ApplicationStartedEvent> {
    private static final Logger log = LoggerFactory.getLogger(WarmupDatabasePopulation.class);
    @Value("#{'${available.subjects}'.split(',')}")
    public List<String> subjects;
    private final SchoolRepository schoolRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final SubjectRepository subjectRepository;

    public WarmupDatabasePopulation(SchoolRepository schoolRepository, SchoolClassRepository schoolClassRepository, SubjectRepository subjectRepository) {
        this.schoolRepository = schoolRepository;
        this.schoolClassRepository = schoolClassRepository;
        this.subjectRepository = subjectRepository;
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
        SchoolClass c1 = schoolClassRepository.save(new SchoolClass("Class 1", subjects.stream().map(sub -> subjectRepository.save(new Subject(sub))).toList()));
        SchoolClass c2 = schoolClassRepository.save(new SchoolClass("Class 2", subjects.stream().map(sub -> subjectRepository.save(new Subject(sub))).toList()));
        SchoolClass c3 = schoolClassRepository.save(new SchoolClass("Class 3", subjects.stream().map(sub -> subjectRepository.save(new Subject(sub))).toList()));
        savedSchool.getSchoolClasses().add(c1);
        savedSchool.getSchoolClasses().add(c2);
        savedSchool.getSchoolClasses().add(c3);
        schoolRepository.save(savedSchool);

        log.info("Application warmup school saved to database!");
        log.debug("Saved School: {}", savedSchool);
    }
}
