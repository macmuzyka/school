package com.school.model.dto;

import com.school.model.entity.SchoolClass;

import java.util.List;

public class ClassDTO {
    private final String className;
    private final List<StudentDTO> classStudents;

    @Override
    public String toString() {
        return "SimpleClassDTO{" +
                "className='" + className + '\'' +
                ", classStudents=" + classStudents +
                '}';
    }

    public ClassDTO(SchoolClass schoolClass) {
        this.className = schoolClass.className();
        this.classStudents = schoolClass.getClassStudents().stream().map(StudentDTO::new).toList();
    }

    public String getClassName() {
        return className;
    }

    public List<StudentDTO> getClassStudents() {
        return classStudents;
    }
}
