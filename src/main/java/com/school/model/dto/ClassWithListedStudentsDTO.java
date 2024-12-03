package com.school.model.dto;

import java.util.List;

public class ClassWithListedStudentsDTO {
    private long numberOfStudents;
    private String className;
    private List<String> students;

    public ClassWithListedStudentsDTO(long numberOfStudents, String className, List<String> students) {
        this.numberOfStudents = numberOfStudents;
        this.className = className;
        this.students = students;
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

    public List<String> getStudents() {
        return students;
    }

    public void setStudents(List<String> students) {
        this.students = students;
    }
}
