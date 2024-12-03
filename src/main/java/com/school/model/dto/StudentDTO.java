package com.school.model.dto;

import com.school.model.entity.Student;
import com.school.model.entity.StudentDuplicateError;
import com.school.model.entity.StudentInsertError;

import java.time.LocalDate;
import java.util.Objects;

public class StudentDTO {
    private long id;
    private String name;
    private String surname;
    private String identifier;
    private LocalDate birthDate;
    private String code;
    private boolean assigned;

    public StudentDTO(Student student) {
        this.id = student.getId();
        this.name = student.getName();
        this.surname = student.getSurname();
        this.identifier = student.getIdentifier();
        this.code = student.getCode();
        this.assigned = student.isAssigned();
        this.birthDate = student.getBirthDate();
    }

    public StudentDTO(StudentInsertError error) {
        this.id = error.getId();
        this.name = error.getName();
        this.surname = error.getSurname();
        this.identifier = error.getIdentifier();
        this.code = error.getCode();
        this.assigned = error.isAssigned();
        this.birthDate = error.getBirthDate();
    }

    public StudentDTO(StudentDuplicateError duplicate) {
        this.id = duplicate.getId();
        this.name = duplicate.getName();
        this.surname = duplicate.getSurname();
        this.identifier = duplicate.getIdentifier();
        this.code = duplicate.getCode();
        this.assigned = duplicate.isAssigned();
        this.birthDate = duplicate.getBirthDate();
    }

    public StudentDTO() {
    }

    @Override
    public String toString() {
        return "StudentDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", identifier='" + identifier + '\'' +
                ", code='" + code + '\'' +
                ", birthDate='" + birthDate + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentDTO that = (StudentDTO) o;
        return Objects.equals(identifier, that.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getCode() {
        return code;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public String getIdentifier() {
        return identifier;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public long getId() {
        return id;
    }
}
