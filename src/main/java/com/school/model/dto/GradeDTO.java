package com.school.model.dto;

import com.school.model.entity.Grade;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GradeDTO {
    private int value;
    private Long subjectId;
    private Long studentId;
    private String note;
    private String gradeType;
    private String createdAt;



    public GradeDTO(int value, Long subjectId, Long studentId, String note, String gradeType) {
        this.value = value;
        this.subjectId = subjectId;
        this.studentId = studentId;
        this.note = note;
        this.gradeType = gradeType;
    }

    public GradeDTO(Grade grade) {
        this.value = grade.getGradeValue();
        this.subjectId = grade.getSubject().getId();
        this.studentId = grade.getStudent().getId();
        this.note = grade.getNote();
        this.gradeType = grade.getGradeType();
        this.createdAt = grade.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
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

    public String getNote() {
        return note;
    }

    public String getGradeType() {
        return gradeType;
    }

    public String getCreatedAt() {
        return createdAt;
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
