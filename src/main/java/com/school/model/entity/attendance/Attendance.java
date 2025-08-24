package com.school.model.entity.attendance;

import com.school.model.entity.Audit;
import com.school.model.entity.Subject;
import com.school.model.entity.classschedule.TimeSlot;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "attendance")
public class Attendance extends Audit {
    private LocalDate presenceTimeframeStart;
    private LocalDate presenceTimeframeEnd;
    @ManyToOne(optional = false)
    private Subject subject;
    @ManyToOne(optional = false)
    private TimeSlot timeSlot;
    @OneToMany(mappedBy = "attendance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentPresence> presenceList = new ArrayList<>();

    public Attendance(LocalDate presenceTimeframeStart, LocalDate presenceTimeframeEnd, Subject subject, TimeSlot timeSlot, List<StudentPresence> presenceList) {
        this.presenceTimeframeStart = presenceTimeframeStart;
        this.presenceTimeframeEnd = presenceTimeframeEnd;
        this.subject = subject;
        this.timeSlot = timeSlot;
        this.presenceList = presenceList;
    }

    public Attendance() {
    }

    public LocalDate getPresenceTimeframeStart() {
        return presenceTimeframeStart;
    }

    public LocalDate getPresenceTimeframeEnd() {
        return presenceTimeframeEnd;
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
