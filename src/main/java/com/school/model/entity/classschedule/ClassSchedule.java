package com.school.model.entity.classschedule;

import com.school.model.entity.Audit;
import com.school.model.entity.SchoolClass;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

//TODO: shouldn't class schedule be holding SchoolClass, not ScheduleEntry?
@Entity
@Table(name = "class_schedule")
public class ClassSchedule extends Audit {
    private String classScheduleName;
    @OneToMany(mappedBy = "classSchedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleEntry> scheduleEntries = new ArrayList<>();
    @OneToOne
    @JoinColumn(name = "school_class_id")
    private SchoolClass schoolClass;

    public ClassSchedule(String classScheduleName, List<ScheduleEntry> scheduleEntries, SchoolClass schoolClass) {
        this.classScheduleName = classScheduleName;
        this.scheduleEntries = scheduleEntries;
        this.schoolClass = schoolClass;
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(final SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
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
