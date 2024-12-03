package com.school.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "student_insert_error")
@EntityListeners(AuditingEntityListener.class)
public class StudentInsertError extends AbstractErrorStudentEntity {

    public StudentInsertError(Student student, String errorDescription, String errorCause) {
        super(student.getName(), student.getSurname(), student.getIdentifier(), student.getCode(), student.getBirthDate(), student.isAssigned(), errorDescription, errorCause);
    }

    public StudentInsertError() {
    }
}
