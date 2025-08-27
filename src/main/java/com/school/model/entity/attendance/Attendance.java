package com.school.model.entity.attendance;

import com.school.model.entity.Audit;
import com.school.model.entity.Subject;
import com.school.model.entity.classschedule.TimeSlot;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "attendance")
public class Attendance extends Audit {
    private LocalTime attendanceTimeframeStart;
    private LocalTime attendanceTimeframeEnd;
    private LocalDate attendanceDate;
    @ManyToOne(optional = false)
    private Subject subject;
    @ManyToOne(optional = false)
    private TimeSlot timeSlot;
    @OneToMany(mappedBy = "attendance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentPresence> presenceList = new ArrayList<>();

    public Attendance(LocalTime attendanceTimeframeStart, LocalTime attendanceTimeframeEnd, LocalDate attendanceDate, Subject subject, TimeSlot timeSlot, List<StudentPresence> presenceList) {
        this.attendanceTimeframeStart = attendanceTimeframeStart;
        this.attendanceTimeframeEnd = attendanceTimeframeEnd;
        this.attendanceDate = attendanceDate;
        this.subject = subject;
        this.timeSlot = timeSlot;
        this.presenceList = presenceList;
    }

    public Attendance() {
    }

    public LocalTime getAttendanceTimeframeStart() {
        return attendanceTimeframeStart;
    }

    public LocalTime getAttendanceTimeframeEnd() {
        return attendanceTimeframeEnd;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public Subject getSubject() {
        return subject;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public List<StudentPresence> getPresenceList() {
        return presenceList;
    }
}
