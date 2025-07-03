package com.school.model.entity.classschedule;

import com.school.model.entity.Audit;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "class_schedule")
public class ClassSchedule extends Audit {
    private String classScheduleName;
    @OneToMany(mappedBy = "classSchedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleEntry> scheduleEntries;

    public ClassSchedule(String classScheduleName, List<ScheduleEntry> scheduleEntries) {
        this.classScheduleName = classScheduleName;
        this.scheduleEntries = scheduleEntries;
    }

    public ClassSchedule() {
    }

    public String getClassScheduleName() {
        return classScheduleName;
    }

    public List<ScheduleEntry> getScheduleEntries() {
        return scheduleEntries;
    }

    public void setClassScheduleName(final String classScheduleName) {
        this.classScheduleName = classScheduleName;
    }

    public void setScheduleEntries(List<ScheduleEntry> scheduleEntries) {
        this.scheduleEntries = scheduleEntries;
    }

    @Override
    public String toString() {
        return "ClassSchedule{" +
                "classScheduleName='" + classScheduleName + '\'' +
                ", scheduleEntries=" + scheduleEntries.get(0).getTimeSlots().stream().map(it -> "\nB:" + it.getStartTime() + " E:" + it.getEndTime()).toList() +
                '}';
    }
}
