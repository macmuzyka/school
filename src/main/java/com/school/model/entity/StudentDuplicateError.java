package com.school.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "student_duplicate_error")
@EntityListeners(AuditingEntityListener.class)
public class StudentDuplicateError extends AbstractErrorStudentEntity {

    public StudentDuplicateError(Student student, String errorDescription, String errorCause) {
        super(student.getName(), student.getSurname(), student.getIdentifier(), student.getCode(), student.getBirthDate(), student.isAssigned(), errorDescription, errorCause);
    }

    public StudentDuplicateError() {
    }
}
