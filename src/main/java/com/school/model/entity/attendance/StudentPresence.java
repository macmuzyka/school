package com.school.model.entity.attendance;

import com.school.model.entity.Audit;
import com.school.model.entity.Student;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "student_presence")
public class StudentPresence extends Audit {
    @ManyToOne(optional = false)
    private Student student;
    @ManyToOne(optional = false)
    private Attendance attendance;
    private boolean present;
    private String note;

    public StudentPresence(Student student, Attendance attendance, boolean present, String note) {
        this.student = student;
        this.attendance = attendance;
        this.present = present;
        this.note = note;
    }

    public StudentPresence() {
    }

    public Student getStudent() {
        return student;
    }

    public Attendance getAttendance() {
        return attendance;
    }

    public boolean isPresent() {
        return present;
    }

    public String getNote() {
        return note;
    }
}
