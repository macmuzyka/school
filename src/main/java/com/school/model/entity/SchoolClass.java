package com.school.model.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "school_class")
public class SchoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String name;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "school_class_id")
    private List<Student> classStudents;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "school_class_id")
    private List<Subject> classSubjects;

    public SchoolClass(String name, List<Subject> classSubjects) {
        this.name = name;
        this.classStudents = new ArrayList<>();
        this.classSubjects = classSubjects;
    }

    public SchoolClass() {}

    public boolean assignStudent2Class(Student student) {
        return classStudents.add(student);
    }

    @Override
    public String toString() {
        return "SchoolClass{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", classStudents=" + classStudents +
                ", classSubjects=" + classSubjects +
                '}';
    }

    public String simpleDisplay() {
        return "SchoolClass{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", classStudents=" + classStudents.stream().map(Student::simpleDisplay).toList() +
                '}';
    }

    public String className() {
        return this.name;
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

    public List<Student> getClassStudents() {
        return classStudents;
    }

    public void setClassStudents(List<Student> classStudents) {
        this.classStudents = classStudents;
    }

    public List<Subject> getClassSubjects() {
        return classSubjects;
    }

    public void setClassSubjects(List<Subject> classSubjects) {
        this.classSubjects = classSubjects;
    }
}
