package com.school.model.nosql.mongodb;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "students")
public class StudentDocument {
    @Id
    private final String id;
    private final String name;
    private final int age;
//    private final String country;
//    private final String gender;

    public StudentDocument(String id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
//        this.country = country;
//        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

//    public String getCountry() {
//        return country;
//    }
//
//    public String getGender() {
//        return gender;
//    }
}
