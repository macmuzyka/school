package com.school.model.entity;

import com.school.model.entity.classschedule.ClassSchedule;
import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "school_class")
public class SchoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id = 0L;
    @Column(unique = true)
    private String name;
    @OneToMany(mappedBy = "schoolClass", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 10)
    private Set<Student> classStudents = new HashSet<>();
    @OneToMany(mappedBy = "schoolClass", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 10)
    private Set<Subject> classSubjects;
    @OneToOne(mappedBy = "schoolClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private ClassSchedule classSchedule;
    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    public SchoolClass(String name, Set<Subject> classSubjects) {
        this.name = name;
        this.classStudents = new HashSet<>();
        this.classSubjects = classSubjects;
    }

    public SchoolClass() {
    }

    public SchoolClass(String name) {
        this.name = name;
    }

    public ClassSchedule getClassSchedule() {
        return classSchedule;
    }

    public void setClassSchedule(ClassSchedule classSchedule) {
        this.classSchedule = classSchedule;
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

    public Set<Student> getClassStudents() {
        if (classStudents == null) {
            return new HashSet<>();
        }
        return classStudents;
    }

    public void setClassStudents(Set<Student> classStudents) {
        this.classStudents = classStudents;
    }

    public Set<Subject> getClassSubjects() {
        return classSubjects;
    }

    public void setClassSubjects(Set<Subject> classSubjects) {
        this.classSubjects = classSubjects;
    }

    public void setSchool(final School school) {
        this.school = school;
    }
}
