package com.school.model.entity;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "student")
@EntityListeners(AuditingEntityListener.class)
public class Student extends AbstractStudentEntity {
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Grade> studentGrades;

    @ManyToOne
    @JoinColumn(name = "school_class_id")
    private SchoolClass schoolClass;

    public List<Grade> getStudentGrades() {
        return studentGrades;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public Student(String name, String surname, String identifier, String code, LocalDate birthDate, boolean assigned) {
        super(name, surname, identifier, code, birthDate, assigned);
    }

    public Student() {
    }

    public void setStudentGrades(List<Grade> studentGrades) {
        this.studentGrades = studentGrades;
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }


}
