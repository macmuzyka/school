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
class ConfigValidator {
    private final ApplicationConfig applicationConfig;
    private final ClassScheduleConfig classScheduleConfig;
    private final Logger log = LoggerFactory.getLogger(ConfigValidator.class);

    public ConfigValidator(ApplicationConfig applicationConfig, ClassScheduleConfig classScheduleConfig) {
        this.applicationConfig = applicationConfig;
        this.classScheduleConfig = classScheduleConfig;
    }

    @PostConstruct
    public void validateConfig() {
        log.info("Running validation config from application.properties file...");
        int subjectCount = applicationConfig.getAvailableSubjects().size();
        int classesToDispense = subjectCount * classScheduleConfig.getMaxSubjectClassPerWeek();
        int days = EnumSet.range(DayOfWeek.MONDAY, DayOfWeek.FRIDAY).size();
        int classScheduleVolume = days * classScheduleConfig.getMaxClassPerDay();
        log.info("Subjects count: {}", subjectCount);
        log.info("Classes to dispense: {}", classesToDispense);
        log.info("Class schedule volume: {}", classScheduleVolume);

        if (classesToDispense > classScheduleVolume) {
            throw new IllegalArgumentException("Classes to dispense exceed class schedule volume, thus configuration is not proper");
        } else {
            log.info("Max classes do not exceed class schedule volume, thus configuration proper");
        }
    }
}
