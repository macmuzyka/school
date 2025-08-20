package com.school.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.LocalTime;

@ConfigurationProperties(prefix = "class.schedule.config")
public class ClassScheduleConfig {
    private final Integer scheduleStart;
    private final Integer lessonDuration;
    private final Integer latestClassScheduleStart;
    private final Integer maxClassPerDay;
    private final Integer maxSubjectClassPerWeek;
    private final Integer shortBreakDuration;
    private final Integer longBreakDuration;
    private final Integer firstLongBreak;
    private final Integer secondLongBreak;

    public ClassScheduleConfig(Integer scheduleStart, Integer lessonDuration, Integer latestClassScheduleStart, Integer maxSubjectClassPerWeek, Integer maxClassPerDay, Integer shortBreakDuration, Integer longBreakDuration, Integer firstLongBreak, Integer secondLongBreak) {
        this.scheduleStart = scheduleStart;
        this.lessonDuration = lessonDuration;
        this.latestClassScheduleStart = latestClassScheduleStart;
        this.maxSubjectClassPerWeek = maxSubjectClassPerWeek;
        this.maxClassPerDay = maxClassPerDay;
        this.shortBreakDuration = shortBreakDuration;
        this.longBreakDuration = longBreakDuration;
        this.firstLongBreak = firstLongBreak;
        this.secondLongBreak = secondLongBreak;
    }

    public LocalTime getScheduleStart() {
        return LocalTime.of(scheduleStart, 0, 0);
    }

    public Integer getLessonDuration() {
        return lessonDuration;
    }

    public Integer getLatestClassScheduleStart() {
        return latestClassScheduleStart;
    }

    public Integer getLatestClassScheduleStartAsIndex() {
        return latestClassScheduleStart - 1;
    }

    public Integer getMaxSubjectClassPerWeek() {
        return maxSubjectClassPerWeek;
    }

    public Integer getMaxClassPerDay() {
        return maxClassPerDay;
    }

    public Integer getShortBreakDuration() {
        return shortBreakDuration;
    }

    public Integer getLongBreakDuration() {
        return longBreakDuration;
    }

    public Integer getFirstLongBreak() {
        return firstLongBreak;
    }

    public Integer getSecondLongBreak() {
        return secondLongBreak;
    }
}
