package com.school.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@ConfigurationProperties(prefix = "app.config")
public class ApplicationConfig {
    private final String availableGrades;
    private final String availableSubjects;
    private final int classMaxSize;
    private final Set<String> gradeTypes;
    private final int minimumStudentTags;
    private final String progressRecordsDirectory;
    private final String progressRecordsFile;
    private final Long maxPreviousNotes;
    private final int gradesToAdd;

    public ApplicationConfig(
            String availableGrades,
            String availableSubjects,
            int classMaxSize,
            int minimumStudentTags,
            int gradesToAdd,
            Set<String> gradeTypes,
            String progressRecordsDirectory,
            String progressRecordsFile,
            Long maxPreviousNotes
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
}
