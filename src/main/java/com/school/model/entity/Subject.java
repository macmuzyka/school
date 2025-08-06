package com.school.model.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "subject")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "school_class_id")
    private SchoolClass schoolClass;

    public Subject(String name, SchoolClass schoolClass) {
        this.name = name;
        this.schoolClass = schoolClass;
    }

    public Subject(String name) {
        this.name = name;
    }

    public Subject() {}

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
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

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final Subject subject)) return false;
        return id == subject.id && Objects.equals(name, subject.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
