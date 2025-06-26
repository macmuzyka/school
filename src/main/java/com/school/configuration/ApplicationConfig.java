package com.school.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@ConfigurationProperties(prefix = "app.config")
public class ApplicationConfig {
    private final String availableGrades;
    private final String availableSubjects;
    private final int classMaxSize;
    private final int minimumStudentTags;
    private final int gradesToAdd;
    private final Set<String> gradeTypes;
    private final String progressRecordsDirectory;
    private final String progressRecordsFile;
    private final Long maxPreviousNotes;
    private final int lessonScheduleStart;
    private final int lessonDuration;
    private final int maxLessons;
    private final int shortBreakDuration;
    private final int longBreakDuration;
    private final int firstLongBreak;
    private final int secondLongBreak;


    public ApplicationConfig(
            String availableGrades,
            String availableSubjects,
            int classMaxSize,
            int minimumStudentTags,
            int gradesToAdd,
            Set<String> gradeTypes,
            String progressRecordsDirectory,
            String progressRecordsFile,
            Long maxPreviousNotes,
            int lessonScheduleStart,
            int lessonDuration,
            int maxLessons,
            int shortBreakDuration,
            int longBreakDuration,
            int firstLongBreak,
            int secondLongBreak
    ) {
        this.availableGrades = availableGrades;
        this.availableSubjects = availableSubjects;
        this.classMaxSize = classMaxSize;
        this.minimumStudentTags = minimumStudentTags;
        this.gradesToAdd = gradesToAdd;
        this.gradeTypes = gradeTypes;
        this.progressRecordsDirectory = progressRecordsDirectory;
        this.progressRecordsFile = progressRecordsFile;
        this.maxPreviousNotes = maxPreviousNotes;
        this.lessonScheduleStart = lessonScheduleStart;
        this.lessonDuration = lessonDuration;
        this.maxLessons = maxLessons;
        this.shortBreakDuration = shortBreakDuration;
        this.longBreakDuration = longBreakDuration;
        this.firstLongBreak = firstLongBreak;
        this.secondLongBreak = secondLongBreak;
    }

    public List<Integer> getAvailableGrades() {
        return Arrays.stream(availableGrades.split(",")).map(Integer::valueOf).toList();
    }

    public List<String> getAvailableSubjects() {
        return Arrays.stream(availableSubjects.split(",")).toList();
    }

    public int getClassMaxSize() {
        return classMaxSize;
    }

    public int getMinimumStudentTags() {
        return minimumStudentTags;
    }

    public int getGradesToAdd() {
        return gradesToAdd;
    }

    public String getProgressRecordsDirectory() {
        return progressRecordsDirectory;
    }

    public String getProgressRecordsFile() {
        return progressRecordsFile;
    }

    public Set<String> getGradeTypes() {
        return gradeTypes;
    }

    public Long getMaxPreviousNotes() {
        return maxPreviousNotes;
    }

    public LocalTime getLessonScheduleStart() {
        return LocalTime.of(lessonScheduleStart, 0, 0);
    }

    public int getLessonDuration() {
        return lessonDuration;
    }

    public int getMaxLessons() {
        return maxLessons;
    }

    public int getShortBreakDuration() {
        return shortBreakDuration;
    }

    public int getLongBreakDuration() {
        return longBreakDuration;
    }

    public int getFirstLongBreak() {
        return firstLongBreak;
    }

    public int getSecondLongBreak() {
        return secondLongBreak;
    }
}
