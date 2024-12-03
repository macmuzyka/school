package com.school.model.dto;

import java.time.LocalDate;

public class NewStudentWithClassDTO {
    private String name;
    private String surname;
    private String identifier;
    private String className;
    private LocalDate birthDate;

    public NewStudentWithClassDTO() {}

    public NewStudentWithClassDTO(String name, String surname, String identifier, String className, LocalDate birtDate) {
        this.name = name;
        this.surname = surname;
        this.identifier = identifier;
        this.className = className;
        this.birthDate = birtDate;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getClassName() {
        return className;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getIdentifier() {
        return identifier;
    }
}
