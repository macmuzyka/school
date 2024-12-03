package com.school.model.dto;

import com.school.model.entity.Grade;

public class GradeDTO {
    private int value;
    private Long subjectId;
    private Long studentId;

    public GradeDTO(int value, Long subjectId, Long studentId) {
        this.value = value;
        this.subjectId = subjectId;
        this.studentId = studentId;
    }

    public GradeDTO(Grade grade) {
        this.value = grade.getGradeValue();
        this.subjectId = grade.getSubject().getId();
        this.studentId = grade.getStudent().getId();
    }

    public GradeDTO() {
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public Long getStudentId() {
        return studentId;
    }

    @Override
    public String toString() {
        return "GradeDTO{" +
                "value=" + value +
                ", subjectId=" + subjectId +
                ", studentId=" + studentId +
                '}';
    }
}
