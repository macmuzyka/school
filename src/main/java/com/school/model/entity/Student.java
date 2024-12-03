package com.school.model.entity;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "student")
@EntityListeners(AuditingEntityListener.class)
public class Student extends AbstractStudentEntity {
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    private List<Grade> studentGrades;

    public List<Grade> getStudentGrades() {
        return studentGrades;
    }

    public Student(String name, String surname, String identifier, String code, LocalDate birthDate, boolean assigned) {
        super(name, surname, identifier, code, birthDate, assigned);
    }

    public Student() {
    }

    public void setStudentGrades(List<Grade> studentGrades) {
        this.studentGrades = studentGrades;
    }
}
