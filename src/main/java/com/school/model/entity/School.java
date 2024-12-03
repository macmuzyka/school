package com.school.model.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @OneToMany(orphanRemoval = true, cascade = {CascadeType.ALL})
    @JoinColumn(name = "school_id")
    private List<SchoolClass> schoolClasses;

    public School(String name, List<SchoolClass> schoolClasses) {
        this.name = name;
        this.schoolClasses = schoolClasses;
    }

    public School() {}

    @Override
    public String toString() {
        return "School{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", schoolClasses=" + schoolClasses +
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

    public List<SchoolClass> getSchoolClasses() {
        return schoolClasses;
    }

    public void setSchoolClasses(List<SchoolClass> schoolClasses) {
        this.schoolClasses = schoolClasses;
    }
}
