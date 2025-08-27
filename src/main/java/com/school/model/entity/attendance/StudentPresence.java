package com.school.model.entity.attendance;

import com.school.model.entity.Audit;
import com.school.model.entity.Student;
import com.school.service.attendance.PresenceStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "student_presence")
public class StudentPresence extends Audit {
    @ManyToOne(optional = false)
    private Student student;
    @ManyToOne(optional = false)
    private Attendance attendance;
    @Enumerated(EnumType.STRING)
    private PresenceStatus presenceStatus;
    private String note;

    public StudentPresence(Student student, Attendance attendance, PresenceStatus presenceStatus, String note) {
        this.student = student;
        this.attendance = attendance;
        this.presenceStatus = presenceStatus;
        this.note = note;
    }

    public StudentPresence(Student student, PresenceStatus present, String note) {
        this.student = student;
        this.presenceStatus = present;
        this.note = note;
    }

    public void setAttendance(final Attendance attendance) {
        this.attendance = attendance;
    }

    public StudentPresence() {
    }

    public Student getStudent() {
        return student;
    }

    public Attendance getAttendance() {
        return attendance;
    }

    public PresenceStatus presenceStatus() {
        return presenceStatus;
    }

    public String getNote() {
        return note;
    }
}
