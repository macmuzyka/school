package com.school.model.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "school_class")
public class SchoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id = 0L;
    @Column(unique = true)
    private String name;
    @OneToMany(mappedBy = "schoolClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Student> classStudents;
    @OneToMany(mappedBy = "schoolClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Subject> classSubjects;
    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    public SchoolClass(String name, List<Subject> classSubjects) {
        this.name = name;
        this.classStudents = new ArrayList<>();
        this.classSubjects = classSubjects;
    }

    public SchoolClass() {}

    public SchoolClass(String name) {
        this.name = name;
        this.id = 0L;
    }

    @Override
    public String toString() {
        return "SchoolClass{" +
                "id=" + id +
                ", name='" + name + '\'' +
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
        if (classStudents == null) {
            return new ArrayList<>();
        }
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

    public void setSchool(final School school) {
        this.school = school;
    }
}
