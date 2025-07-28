package com.school.model.entity.classschedule;

import com.school.model.entity.Audit;
import jakarta.persistence.*;

@Entity
@Table(name = "class_room")
public class ClassRoom extends Audit {
    @Column(nullable = false)
    private Integer roomNumber;
    @OneToOne(mappedBy = "classRoom")
    private TimeSlot timeSlot = null;
    private boolean occupied = false;

    public ClassRoom(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public ClassRoom() {
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void assignTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
        this.occupied = true;
    }

    public void detachFromTimeSlot() {
        this.timeSlot = null;
        this.occupied = false;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public boolean isOccupied() {
        return occupied;
    }
}
