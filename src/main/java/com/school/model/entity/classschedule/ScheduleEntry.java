package com.school.model.entity.classschedule;

import com.school.model.entity.Audit;
import com.school.model.entity.SchoolClass;
import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "schedule_entry")
public class ScheduleEntry extends Audit {
    @ManyToOne
    @JoinColumn(name = "class_schedule_id")
    private ClassSchedule classSchedule;
    @ManyToOne
    @JoinColumn(name = "school_class_id")
    private SchoolClass schoolClass;
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;
    @OneToMany(mappedBy = "scheduleEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeSlot> timeSlots;

    public ScheduleEntry(ClassSchedule classSchedule, SchoolClass schoolClass, DayOfWeek dayOfWeek, LinkedList<TimeSlot> timeSlots) {
        this.classSchedule = classSchedule;
        this.schoolClass = schoolClass;
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

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public void setClassSchedule(final ClassSchedule classSchedule) {
        this.classSchedule = classSchedule;
    }

    public void setTimeSlots(final LinkedList<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }
}
