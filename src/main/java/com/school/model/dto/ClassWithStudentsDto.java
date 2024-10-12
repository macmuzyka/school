package com.school.model.dto;

import java.util.List;

public class ClassWithStudentsDto {
    private long numberOfStudents;
    private String className;
    private List<String> studentNames;

    public ClassWithStudentsDto(long numberOfStudents, String className, List<String> studentNames) {
        this.numberOfStudents = numberOfStudents;
        this.className = className;
        this.studentNames = studentNames;
    }

    public long getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(long numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<String> getStudentNames() {
        return studentNames;
    }

    public void setStudentNames(List<String> studentNames) {
        this.studentNames = studentNames;
    }
}
