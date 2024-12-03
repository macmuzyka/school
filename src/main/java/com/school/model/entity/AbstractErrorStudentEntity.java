package com.school.model.entity;

import jakarta.persistence.MappedSuperclass;

import java.time.LocalDate;

@MappedSuperclass
public abstract class AbstractErrorStudentEntity extends AbstractStudentEntity {
    private String errorDescription;
    private String errorCause;

    public AbstractErrorStudentEntity(String name, String surname, String identifier, String code, LocalDate birthDate, boolean assigned, String errorDescription, String errorCause) {
        super(name, surname, identifier, code, birthDate, assigned);
        this.errorDescription = errorDescription;
        this.errorCause = errorCause;
    }

    public AbstractErrorStudentEntity() {
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getErrorCause() {
        return errorCause;
    }

    public void setErrorCause(String errorCause) {
        this.errorCause = errorCause;
    }
}
