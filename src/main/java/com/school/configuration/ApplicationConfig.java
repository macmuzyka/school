package com.school.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;

@ConfigurationProperties(prefix = "app.config")
public class ApplicationConfig {
    private String availableGrades;
    private String availableSubjects;
    private int classMaxSize;
    private int minimumStudentTags;
    private int gradesToAdd;

    public ApplicationConfig(String availableGrades, String availableSubjects, int classMaxSize, int minimumStudentTags, int gradesToAdd) {
        this.availableGrades = availableGrades;
        this.availableSubjects = availableSubjects;
        this.classMaxSize = classMaxSize;
        this.minimumStudentTags = minimumStudentTags;
        this.gradesToAdd = gradesToAdd;
    }

    public List<Integer> getAvailableGrades() {
        return Arrays.stream(availableGrades.split(",")).map(Integer::valueOf).toList();
    }

    public void setAvailableGrades(String availableGrades) {
        this.availableGrades = availableGrades;
    }

    public List<String> getAvailableSubjects() {
        return Arrays.stream(availableSubjects.split(",")).toList();
    }

    public void setAvailableSubjects(String availableSubjects) {
        this.availableSubjects = availableSubjects;
    }

    public int getClassMaxSize() {
        return classMaxSize;
    }

    public void setClassMaxSize(int classMaxSize) {
        this.classMaxSize = classMaxSize;
    }

    public int getMinimumStudentTags() {
        return minimumStudentTags;
    }

    public void setMinimumStudentTags(int minimumStudentTags) {
        this.minimumStudentTags = minimumStudentTags;
    }

    public int getGradesToAdd() {
        return gradesToAdd;
    }

    public void setGradesToAdd(int gradesToAdd) {
        this.gradesToAdd = gradesToAdd;
    }
}
