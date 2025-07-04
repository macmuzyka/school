package com.school.model.entity.classschedule;

import com.school.model.entity.Audit;
import com.school.model.entity.Subject;
import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "time_slot")
public class TimeSlot extends Audit {
    @ManyToOne
    @JoinColumn(name = "schedule_entry_id")
    private ScheduleEntry scheduleEntry;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    private LocalTime startTime;
    private LocalTime endTime;

    private boolean isBreak;

    public TimeSlot(LocalTime startTime, LocalTime endTime, Subject subject, ScheduleEntry scheduleEntry, boolean isBreak) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.subject = subject;
        this.scheduleEntry = scheduleEntry;
        this.isBreak = isBreak;
    }

    public TimeSlot() {
    }

    @Override
    public String toString() {
        return "TimeSlot{" +
                "scheduleEntry=" + scheduleEntry.getId() +
                ", subject=" + (subject == null ? "NO SUBJECT ASSIGNED" : subject.getId() + " : " + subject.getName()) +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", isBreak=" + isBreak +
                '}';
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public Subject getSubject() {
        return subject;
    }

    public boolean isBreak() {
        return isBreak;
    }

    public boolean isNotBreak() {
        return !isBreak;
    }

    public void setSubject(final Subject subject) {
        this.subject = subject;
    }

    public String getDurationDisplay() {
        return startTime + "-" + endTime;
    }
}
