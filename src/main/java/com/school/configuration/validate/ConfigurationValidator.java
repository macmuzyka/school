package com.school.configuration.validate;

import com.school.configuration.ApplicationConfig;
import com.school.configuration.ClassScheduleConfig;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.EnumSet;

@Component
public class ConfigurationValidator {
    private final ApplicationConfig applicationConfig;
    private final ClassScheduleConfig classScheduleConfig;
    private final Logger log = LoggerFactory.getLogger(ConfigurationValidator.class);

    public ConfigurationValidator(ApplicationConfig applicationConfig, ClassScheduleConfig classScheduleConfig) {
        this.applicationConfig = applicationConfig;
        this.classScheduleConfig = classScheduleConfig;
    }

    @PostConstruct
    public void validateConfig() {
        log.debug("Running validation config from application.properties file...");
        int subjectCount = applicationConfig.getAvailableSubjects().size();
        int classesToDispense = subjectCount * classScheduleConfig.getMaxSubjectClassPerWeek();
        int days = EnumSet.range(DayOfWeek.MONDAY, DayOfWeek.FRIDAY).size();
        int classScheduleVolume = days * classScheduleConfig.getMaxClassPerDay();
        log.debug("Subjects count: {}", subjectCount);
        log.debug("Classes to dispense: {}", classesToDispense);
        log.debug("Class schedule volume: {}", classScheduleVolume);

        if (classesToDispense > classScheduleVolume) {
            throw new IllegalArgumentException("Classes to dispense exceed class schedule volume, thus configuration is not proper");
        } else {
            log.info("Max classes do not exceed class schedule volume, thus configuration proper");
        }
    }

    public Integer expectedNumberOfGeneratedClasses() {
        return applicationConfig.getAvailableSubjects().size() * classScheduleConfig.getMaxSubjectClassPerWeek();
    }
}
