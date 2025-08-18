package com.school.configuration;

/**
 * Created by Raweshau
 * on 12.08.2025
 */

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.LocalTime;

@ConfigurationProperties(prefix = "class.schedule.config")
public class ClassScheduleConfig {
    private final Integer lessonScheduleStart;
    private final Integer lessonDuration;
    private final Integer maxLessonPerDay;
    private final Integer maxLessonPerSubject;
    private final Integer shortBreakDuration;
    private final Integer longBreakDuration;
    private final Integer firstLongBreak;
    private final Integer secondLongBreak;

    public ClassScheduleConfig(Integer lessonScheduleStart, Integer lessonDuration, Integer maxLessonPerSubject, Integer maxLessonPerDay, Integer shortBreakDuration, Integer longBreakDuration, Integer firstLongBreak, Integer secondLongBreak) {
        this.lessonScheduleStart = lessonScheduleStart;
        this.lessonDuration = lessonDuration;
        this.maxLessonPerSubject = maxLessonPerSubject;
        this.maxLessonPerDay = maxLessonPerDay;
        this.shortBreakDuration = shortBreakDuration;
        this.longBreakDuration = longBreakDuration;
        this.firstLongBreak = firstLongBreak;
        this.secondLongBreak = secondLongBreak;
    }

    public LocalTime getLessonScheduleStart() {
        return LocalTime.of(lessonScheduleStart, 0, 0);
    }

    public Integer getLessonDuration() {
        return lessonDuration;
    }

    public Integer getMaxLessonPerSubject() {
        return maxLessonPerSubject;
    }

    public Integer getMaxLessonPerDay() {
        return maxLessonPerDay;
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
