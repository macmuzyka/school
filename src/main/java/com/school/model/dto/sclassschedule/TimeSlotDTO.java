package com.school.model.dto.sclassschedule;

import com.school.model.entity.classschedule.TimeSlot;

import java.time.LocalTime;

public class TimeSlotDTO {
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final String subject;

    public TimeSlotDTO(LocalTime startTime, LocalTime endTime, String subject) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.subject = subject;
    }

    public TimeSlotDTO(TimeSlot timeSlot) {
        this.startTime = timeSlot.getStartTime();
        this.endTime = timeSlot.getEndTime();
        this.subject = timeSlot.getSubject().getName();
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getSubject() {
        return subject;
    }
}
