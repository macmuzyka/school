package com.school.model.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@MappedSuperclass
public abstract class AbstractStudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String surname;
    @Column(nullable = false, unique = true)
    private String identifier;
    @Column(nullable = false, unique = true)
    private String code;
    private boolean assigned;
    @Column(nullable = false)
    private LocalDate birthDate;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime lastUpdatedAt;

    public AbstractStudentEntity(String name, String surname, String identifier, String code, LocalDate birthDate, boolean assigned) {
        this.name = name;
        this.surname = surname;
        this.identifier = identifier;
        this.code = code;
        this.birthDate = birthDate;
        this.assigned = assigned;
    }

    public AbstractStudentEntity() {
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", code='" + code + '\'' +
                ", identifier='" + identifier + '\'' +
                ", assigned=" + assigned +
                '}';
    }

    public String simpleDisplay() {
        return "[name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", code='" + code + '\'' +
                ", identifier=" + identifier + "]";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public int getAge() {
        return calculateAge();
    }

    private int calculateAge() {
        return Period.between(this.birthDate, LocalDate.now()).getYears();
    }
}
