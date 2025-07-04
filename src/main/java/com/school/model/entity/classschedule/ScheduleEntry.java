package com.school.model.entity.classschedule;

import com.school.model.entity.Audit;
import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "schedule_entry")
public class ScheduleEntry extends Audit {
    @ManyToOne
    @JoinColumn(name = "class_schedule_id")
    private ClassSchedule classSchedule;
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;
    @OneToMany(mappedBy = "scheduleEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeSlot> timeSlots = new ArrayList<>();

    public ScheduleEntry(ClassSchedule classSchedule, DayOfWeek dayOfWeek, List<TimeSlot> timeSlots) {
        this.classSchedule = classSchedule;
        this.dayOfWeek = dayOfWeek;
        this.timeSlots = timeSlots;
    }

    public ScheduleEntry() {
    }

    @Override
    public String toString() {
        return "ScheduleEntry{" +
                "classSchedule=" + classSchedule.getId() +
                ", dayOfWeek=" + dayOfWeek +
                ", timeSlots=" + timeSlots +
                '}';
    }

    public ClassSchedule getClassSchedule() {
        return classSchedule;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public void setClassSchedule(ClassSchedule classSchedule) {
        this.classSchedule = classSchedule;
    }

    public void setTimeSlots(List<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }
}
